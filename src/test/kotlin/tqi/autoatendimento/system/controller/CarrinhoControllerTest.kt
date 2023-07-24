package tqi.autoatendimento.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import tqi.autoatendimento.system.dto.CarrinhoDto
import tqi.autoatendimento.system.dto.CategoriaDto
import tqi.autoatendimento.system.dto.ProdutosDto
import tqi.autoatendimento.system.entity.Carrinho
import tqi.autoatendimento.system.entity.Produtos
import tqi.autoatendimento.system.enum.UnidadeMedida
import tqi.autoatendimento.system.repository.CarrinhoRepository
import tqi.autoatendimento.system.repository.CategoriaRepository
import tqi.autoatendimento.system.repository.ProdutosRepository
import java.math.BigDecimal

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
/*
Pesquisando pela internet, vi que o @DirtiesContext não era tão indicado, mas foi o jeito que achei
para que os IDs do banco de dados sofram um TRUNCATE e não gerem interferência. Essa anotação deixa os testes mais lentos.
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CarrinhoControllerTest {

    //Anotação para injetar a dependências
    @Autowired private lateinit var carrinhoRepository: CarrinhoRepository
    @Autowired private lateinit var produtosRepository: ProdutosRepository
    @Autowired private lateinit var categoriaRepository: CategoriaRepository
    @Autowired private lateinit var mockMvc: MockMvc
    @Autowired private lateinit var objectMapper: ObjectMapper

    //endpoint principal do carrinho
    companion object{
        const val URL: String = "/api/carrinho"
    }

    //Deleta tudo antes de cada teste
    @BeforeEach
    fun setup(){
        produtosRepository.deleteAll()
        categoriaRepository.deleteAll()
        carrinhoRepository.deleteAll()
    }

    //Deleta tudo após cada teste
    @AfterEach
    fun tearDown(){
        produtosRepository.deleteAll()
        categoriaRepository.deleteAll()
        carrinhoRepository.deleteAll()
    }

    //POST, método para salvar um produto no carrinho
    @Test
    fun `should save a new carrinho successfully`() {
        //given
        val carrinhoDto: CarrinhoDto = buildCarrinhoDto()
        categoriaRepository.save(buildCategoriaDto().toEntity())
        produtosRepository.save(buildProdutosDto().toEntity())
        val message: String = "O carrinho foi atualizado com sucesso."

        //when
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carrinhoDto)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$").value(message))
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
    }

    //POST, retorna BAD REQUEST se o produto já existir no carrinho
    @Test
    fun `should return BAD REQUEST if produto already exists`() {
        //given
        val carrinhoDto: CarrinhoDto = buildCarrinhoDto()
        categoriaRepository.save(buildCategoriaDto().toEntity())
        produtosRepository.save(buildProdutosDto().toEntity())
        carrinhoRepository.save(carrinhoDto.toEntity())
        val message: String = "Produto já está no carrinho."

        //when
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carrinhoDto)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$").value(message))
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
    }

    //POST, retorna que o produto não tem estoque se a quantidade for maior que a ofertada
    @Test
    fun `should return BAD REQUEST if produto does not have enough stock`() {
        //given
        val carrinhoDto: CarrinhoDto = buildCarrinhoDto(quantidadeProduto = 20)
        categoriaRepository.save(buildCategoriaDto().toEntity())
        produtosRepository.save(buildProdutosDto().toEntity())
        val message: String = "Não há estoque o suficiente para a quantidade solicitada."

        //when
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carrinhoDto)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$").value(message))
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
    }

    //GET, método para retornar todos os produtos do carrinho
    @Test
    fun `should get all produtos from carrinho`() {
        // Given
        categoriaRepository.save(buildCategoriaDto().toEntity())
        produtosRepository.save(buildProdutosDto().toEntity())
        val produto2: Produtos = produtosRepository.save(buildProdutosDto(nome = "Batata", quantidade = 15).toEntity())
        carrinhoRepository.save(buildCarrinhoDto().toEntity())
        carrinhoRepository.save(buildCarrinhoDto(nomeProduto = "Batata", idProduto = produto2.id!!, quantidadeProduto = 14).toEntity())

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.get(URL)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].nomeProduto").value("Pão"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].nomeProduto").value("Batata"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantidadeProduto").value(5))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].quantidadeProduto").value(14))
            .andDo(MockMvcResultHandlers.print())
    }

    //PATCH, método para alterar a quantidade do produto do carrinho
    @Test
    fun `should edit produto from carrinho`() {
        //given
        categoriaRepository.save(buildCategoriaDto().toEntity())
        produtosRepository.save(buildProdutosDto().toEntity())
        carrinhoRepository.save(buildCarrinhoDto().toEntity())
        val carrinhoAlterarDto: CarrinhoDto = buildCarrinhoDto(quantidadeProduto = 8)
        val responseMessage = "O carrinho foi alterado com sucesso."

        mockMvc.perform(MockMvcRequestBuilders.patch("$URL/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(carrinhoAlterarDto)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$").value(responseMessage))
            .andDo(MockMvcResultHandlers.print())
    }

    //DELETE, método para deletar um produto do carrinho
    @Test
    fun `should delete produto from carrinho`() {
        //given
        categoriaRepository.save(buildCategoriaDto().toEntity())
        produtosRepository.save(buildProdutosDto().toEntity())
        val carrinho: Carrinho = carrinhoRepository.save(buildCarrinhoDto().toEntity())

        //when/then
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/${carrinho.id}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    //DELETE, método para deletar todos os produtos do carrinho
    @Test
    fun `should delete all produtos from carrinho`() {
        //given
        categoriaRepository.save(buildCategoriaDto().toEntity())
        produtosRepository.save(buildProdutosDto().toEntity())
        carrinhoRepository.save(buildCarrinhoDto().toEntity())

        //when/then
        mockMvc.perform(MockMvcRequestBuilders.delete(URL)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    private fun buildCarrinhoDto(
        nomeProduto: String = "Pão",
        idProduto: Long = 1,
        quantidadeProduto: Int = 5
    ) = CarrinhoDto(
        nomeProduto = nomeProduto,
        idProduto = idProduto,
        quantidadeProduto = quantidadeProduto
    )

    private fun buildProdutosDto(
        nome: String = "Pão",
        unidadeMedida: UnidadeMedida = UnidadeMedida.UNIDADE,
        precoUnitario: BigDecimal = BigDecimal(5.0),
        categoriaId: Long = 1,
        quantidade: Int = 10
    ) = ProdutosDto(
        nome = nome,
        categoriaId = categoriaId,
        quantidade = quantidade,
        precoUnitario = precoUnitario,
        unidadeDeMedida = unidadeMedida
    )

    private fun buildCategoriaDto(
        nome: String = "Lanches"
    ) = CategoriaDto(
        nome = nome
    )

}