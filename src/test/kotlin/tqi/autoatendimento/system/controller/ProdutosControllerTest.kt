package tqi.autoatendimento.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
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
import tqi.autoatendimento.system.dto.CategoriaDto
import tqi.autoatendimento.system.dto.ProdutosAlterarDto
import tqi.autoatendimento.system.dto.ProdutosDto
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.entity.Produtos
import tqi.autoatendimento.system.enum.UnidadeMedida
import tqi.autoatendimento.system.repository.CategoriaRepository
import tqi.autoatendimento.system.repository.ProdutosRepository
import tqi.autoatendimento.system.service.impl.ProdutosService
import java.lang.IllegalArgumentException
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
class ProdutosControllerTest {
    @Autowired private lateinit var categoriaRepository: CategoriaRepository
    @Autowired private lateinit var produtosRepository: ProdutosRepository
    @Autowired private lateinit var mockMvc: MockMvc
    @Autowired private lateinit var objectMapper: ObjectMapper

    companion object{
        const val URL: String = "/api/produtos"
    }

    @BeforeEach
    fun setup(){
        produtosRepository.deleteAll()
        categoriaRepository.deleteAll()
    }

    @AfterEach
    fun tearDown(){
        produtosRepository.deleteAll()
        categoriaRepository.deleteAll()
    }

    //POST, método para salvar um produto
    @Test
    fun `should save a new produto successfully`() {
        //given
        val produtoDto: ProdutosDto = buildProdutosDto()
        categoriaRepository.save(buildCategoriaDto().toEntity())

        //when
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(produtoDto)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        //then
        val response: String = result.response.contentAsString
        assertThat(response).contains("O produto '${produtoDto.nome}' foi adicionado com sucesso!")
        val produtosFromDB = produtosRepository.findAll()
        assertThat(produtosFromDB).hasSize(1)
        val savedProduto: Produtos = produtosFromDB.first()
        assertThat(savedProduto.nome).isEqualTo(produtoDto.nome)
    }

    //POST, retorna que a categoria não existe se um produto for salvo com uma categoria inexistente
    @Test
    fun `should return BAD REQUEST when trying to save a produto with non-existent category`() {
        // Given
        val categoriaId = 1L
        val produtoDto = buildProdutosDto(categoriaId = categoriaId)
        val message = "Categoria não existe."

        //when/then
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(produtoDto)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$").value(message))
            .andDo(MockMvcResultHandlers.print())
    }

    //GET, método para retornar todos os produtos quando não houver um filtro
    @Test
    fun `should get all produtos when no filters are provided`() {
        //given
        categoriaRepository.save(buildCategoria())
        produtosRepository.save(buildProdutos())
        produtosRepository.save(buildProdutos(id = 2, nome = "Pãozinho"))

        //when/then
        mockMvc.perform(MockMvcRequestBuilders.get(URL)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome").value("Pão"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].nome").value("Pãozinho"))
            .andDo(MockMvcResultHandlers.print())
    }

    //GET, método para retornar todos os produtos quando houver filtro de categoria
    @Test
    fun `should get all produtos when categoria is provided`() {
        //given
        categoriaRepository.save(buildCategoria())
        categoriaRepository.save(buildCategoria(id = 2, nome = "Teste"))
        produtosRepository.save(buildProdutos())
        produtosRepository.save(buildProdutos(id = 2, nome = "Pãozinho"))

        //when/then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL?categoria=2")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].nome").value("Pãozinho"))
            .andDo(MockMvcResultHandlers.print())
    }

    //GET, método para retornar todos os produtos quando houver filtro de nome
    @Test
    fun `should get all produtos when nome is provided`() {
        // Given
        categoriaRepository.save(buildCategoriaDto().toEntity())
        produtosRepository.save(buildProdutos())
        produtosRepository.save(buildProdutos(nome = "Batata"))
        produtosRepository.save(buildProdutos(id = 2, nome = "Batatinha"))

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL?nome=Bat")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome").value("Batata"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].nome").value("Batatinha"))
            .andDo(MockMvcResultHandlers.print())
    }

    //GET, método para retornar o produto pelo id
    @Test
    fun `should return produto by id`() {
        // Given
        categoriaRepository.save(buildCategoriaDto().toEntity())
        produtosRepository.save(buildProdutos())

        mockMvc.perform(MockMvcRequestBuilders.get("$URL/s?id=1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Pão"))
            .andDo(MockMvcResultHandlers.print())
    }

    //PUT, método para alterar um produto
    @Test
    fun `should edit produto`() {
        // Given
        categoriaRepository.save(buildCategoria())
        produtosRepository.save(buildProdutos())
        val produtosAlterarDto: ProdutosAlterarDto = buildProdutosAlterarDto()
        val responseMessage = "O produto de ID: ${produtosAlterarDto.id} foi alterado com sucesso!"

        mockMvc.perform(MockMvcRequestBuilders.put("$URL/alterar")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(produtosAlterarDto)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$").value(responseMessage))
            .andDo(MockMvcResultHandlers.print())
    }

    //DELETE, método para deletar um produto
    @Test
    fun `should delete produto`() {
        ///given
        categoriaRepository.save(buildCategoria())
        val produto: Produtos = produtosRepository.save(buildProdutos())
        val responseMessage = "Produto de ID:${produto.id} excluído com sucesso."

        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/${produto.id}")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$").value(responseMessage))
            .andDo(MockMvcResultHandlers.print())
    }



    private fun buildProdutosDto(
        nome: String = "Pão",
        unidadeMedida: UnidadeMedida = UnidadeMedida.UNIDADE,
        precoUnitario: BigDecimal = BigDecimal(5.0),
        categoriaId: Long = 1,
        quantidade: Int = 5
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

    private fun buildProdutos(
        id: Long = 1,
        nome: String = "Pão",
        precoUnitario: BigDecimal = BigDecimal(6.50),
        quantidade: Int = 3,
        unidadeMedida: UnidadeMedida = UnidadeMedida.UNIDADE,
        categoria: Categoria = buildCategoria()
    ) = Produtos(
        id = id,
        nome = nome,
        precoUnitario = precoUnitario,
        quantidade = quantidade,
        unidadeDeMedida = unidadeMedida,
        categoria = categoria
    )

    private fun buildCategoria(
        id: Long = 1,
        nome: String = "Lanches"
    ) = Categoria(
        id = id,
        nome = nome
    )

    private fun buildProdutosAlterarDto(
        id: Long = 1,
        nome: String = "Pãozinho",
        unidadeMedida: UnidadeMedida = UnidadeMedida.UNIDADE,
        precoUnitario: BigDecimal = BigDecimal(6.0),
        categoriaId: Long = 1,
        quantidade: Int = 10
    ) = ProdutosAlterarDto(
        id = id,
        nome = nome,
        categoriaId = categoriaId,
        quantidade = quantidade,
        precoUnitario = precoUnitario,
        unidadeDeMedida = unidadeMedida
    )

}