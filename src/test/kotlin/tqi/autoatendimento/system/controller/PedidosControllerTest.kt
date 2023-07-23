package tqi.autoatendimento.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers
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
import tqi.autoatendimento.system.dto.FinalizacaoVendaDto
import tqi.autoatendimento.system.dto.ProdutosDto
import tqi.autoatendimento.system.entity.Pedidos
import tqi.autoatendimento.system.enum.FormaPagamento
import tqi.autoatendimento.system.enum.UnidadeMedida
import tqi.autoatendimento.system.finalizacao.FinalizacaoVendaResponse
import tqi.autoatendimento.system.repository.CarrinhoRepository
import tqi.autoatendimento.system.repository.CategoriaRepository
import tqi.autoatendimento.system.repository.PedidosRepository
import tqi.autoatendimento.system.repository.ProdutosRepository
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
/*
Pesquisando pela internet, vi que o @DirtiesContext não era tão indicado, mas foi o jeito que achei
para que os IDs do banco de dados sofram um TRUNCATE e não gerem interferência. Essa anotação deixa os testes mais lentos.
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PedidosControllerTest {

    @Autowired private lateinit var pedidosRepository: PedidosRepository
    @Autowired private lateinit var carrinhoRepository: CarrinhoRepository
    @Autowired private lateinit var produtosRepository: ProdutosRepository
    @Autowired private lateinit var categoriaRepository: CategoriaRepository
    @Autowired private lateinit var mockMvc: MockMvc
    @Autowired private lateinit var objectMapper: ObjectMapper

    companion object{
        const val URL: String = "/api/pedidos"
    }

    @BeforeEach
    fun setup(){
        produtosRepository.deleteAll()
        categoriaRepository.deleteAll()
        carrinhoRepository.deleteAll()
        pedidosRepository.deleteAll()
    }

    @AfterEach
    fun tearDown(){
        produtosRepository.deleteAll()
        categoriaRepository.deleteAll()
        carrinhoRepository.deleteAll()
        produtosRepository.deleteAll()
    }

    //POST, método para finalizar um pedido
    @Test
    fun `should finish a pedido`() {
        //given
        val finalizacaoDto: FinalizacaoVendaDto = buildVenda()

        categoriaRepository.save(buildCategoriaDto().toEntity())
        produtosRepository.save(buildProdutosDto().toEntity())
        produtosRepository.save(buildProdutosDto(nome = "Batata", precoUnitario = BigDecimal(7.5)).toEntity())
        carrinhoRepository.save(buildCarrinhoDto().toEntity())
        carrinhoRepository.save(buildCarrinhoDto("Batata", 2, 2).toEntity())

        //when/then
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(finalizacaoDto)))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.formaPagamento").value("Pagamento realizado via PIX."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem").value(
                "Obrigado(a) por comprar na JuMarket! Volte sempre!!! Leve o papel que contém o código de venda para obter seus produtos no balcão!"))
            .andDo(MockMvcResultHandlers.print())
            .andReturn()

            //Verificar se a resposta é uma instância de FinalizacaoVendaResponse
            val responseString: String = result.response.contentAsString
            val finalizacaoResponse: FinalizacaoVendaResponse? = objectMapper.readValue(responseString, FinalizacaoVendaResponse::class.java)
            assertThat(finalizacaoResponse).isInstanceOf(FinalizacaoVendaResponse::class.java)
    }

    //GET, método para retornar todos os pedidos pelo codVenda
    @Test
    fun `should get all pedidos by codVenda when total is false`() {
        //given
        val codVenda: String = Global.codVenda
        pedidosRepository.save(buildPedido())
        pedidosRepository.save(buildPedido( "Batata", 2, BigDecimal(8.5), 2))
        pedidosRepository.save(buildPedido( "Batatinha", 3, BigDecimal(9.5), 3,
            codVenda = UUID.randomUUID().toString())) //Pedido adicional com CodVenda diferente

        //when/then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL?cod=$codVenda")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<Any>(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].nomeProduto").value("Pão"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].precoUnitario").value(6.00))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantidade").value(6))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].nomeProduto").value("Batata"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].precoUnitario").value(8.50))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].quantidade").value(2))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should get total value by codVenda when total is true`() {
        //given
        pedidosRepository.save(buildPedido())
        pedidosRepository.save(buildPedido( "Batata", 2, BigDecimal(8.5), 2))
        pedidosRepository.save(buildPedido( "Batatinha", 3, BigDecimal(9.5), 3,
            codVenda = UUID.randomUUID().toString())) //Pedido adicional com CodVenda diferente

        val codVenda: String = Global.codVenda
        val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val valorTotal: String = numberFormat.format(14.50)

        //when/then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL?cod=$codVenda&total=true")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            //6.0 do primeiro pedido somado com 8.5 do segundo pedido = 14.50, o valor final já está formatado em reais
            .andExpect(MockMvcResultMatchers.jsonPath("$").value(valorTotal))
            .andDo(MockMvcResultHandlers.print())
    }

    //GET, retorna todos os pedidos
    @Test
    fun `should return all pedidos`() {
        //given
        pedidosRepository.save(buildPedido())
        pedidosRepository.save(buildPedido( "Batata", 2, BigDecimal(8.5), 2))
        pedidosRepository.save(buildPedido( "Batatinha", 3, BigDecimal(9.5), 3,
            codVenda = UUID.randomUUID().toString())) //Pedido adicional com CodVenda diferente

        //when/then
        mockMvc.perform(MockMvcRequestBuilders.get(URL)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<Any>(3)))
            .andDo(MockMvcResultHandlers.print())
    }

    private fun buildVenda(
        formaPagamento: FormaPagamento = FormaPagamento.PIX
    ) = FinalizacaoVendaDto(
        formaPagamento = formaPagamento
    )

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
        precoUnitario: BigDecimal = BigDecimal(6.0),
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

    private fun buildPedido(
        nomeProduto: String = "Pão",
        idProduto: Long? = 1,
        precoUnitario: BigDecimal = buildProdutosDto().toEntity().precoUnitario,
        quantidade: Int = 6,
        formaPagamento: FormaPagamento = FormaPagamento.PIX,
        codVenda: String = Global.codVenda
    ) = Pedidos(
        nomeProduto = nomeProduto,
        idProduto = idProduto,
        precoUnitario = precoUnitario,
        quantidade = quantidade,
        formaPagamento = formaPagamento,
        codVenda = codVenda
    )

}

object Global {
    var codVenda: String = UUID.randomUUID().toString()
}