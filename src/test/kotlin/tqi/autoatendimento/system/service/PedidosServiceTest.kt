package tqi.autoatendimento.system.service

import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import tqi.autoatendimento.system.repository.PedidosRepository
import tqi.autoatendimento.system.service.impl.PedidosService
import org.assertj.core.api.Assertions.*
import tqi.autoatendimento.system.entity.Carrinho
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.entity.Pedidos
import tqi.autoatendimento.system.entity.Produtos
import tqi.autoatendimento.system.enum.FormaPagamento
import tqi.autoatendimento.system.enum.UnidadeMedida
import tqi.autoatendimento.system.repository.ProdutosRepository
import tqi.autoatendimento.system.service.impl.CarrinhoService
import java.math.BigDecimal
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class PedidosServiceTest {
    @MockK lateinit var pedidosRepository: PedidosRepository
    @MockK lateinit var produtosRepository: ProdutosRepository
    @MockK lateinit var carrinhoService: CarrinhoService
    @InjectMockKs lateinit var pedidosService: PedidosService

    //Método para testar se o valor total e codVenda são retornados (esse método foi trabalhoso)
    @Test
    fun `should finish pedido and return total value and codVenda`() {
        //given
        val fakeCarrinho: List<Carrinho> = listOf(
            buildCarrinho(),
            buildCarrinho(id = 2L, idProduto = 2L, nomeProduto = "X-tudo", quantidadeProduto = 3, precoProduto = BigDecimal(5.0))
        )
        every { carrinhoService.findCarrinho() } returns fakeCarrinho
        every { produtosRepository.removerEstoqueProduto(any(), any()) } just runs
        every { carrinhoService.truncateAll() } just runs
        val captor: CapturingSlot<Pedidos> = slot<Pedidos>()
        every { pedidosRepository.save(capture(captor)) } answers { captor.captured }

        //when
        val actual: Array<Any> = pedidosService.finalizarPedido(FormaPagamento.CARTAO_CREDITO)

        //then
        verify { carrinhoService.findCarrinho() }
        fakeCarrinho.forEach {
            verify { produtosRepository.removerEstoqueProduto(it.idProduto, it.quantidadeProduto) }
        }
        verify { carrinhoService.truncateAll() }

        val pedidoCapturado: Pedidos = captor.captured
        assertThat(actual[0]).isEqualTo(BigDecimal(10.5)) // 4,50 + 5,50 = 10
        assertThat(actual[1]).isEqualTo(pedidoCapturado.codVenda)
    }

    //Método para testar se a mensagem de pagamento correta é retornada
    @Test
    fun `should return correct payment method message for each FormaPagamento`() {
        //given
        val cartaoCredito = FormaPagamento.CARTAO_CREDITO
        val cartaoDebito = FormaPagamento.CARTAO_DEBITO
        val dinheiro = FormaPagamento.DINHEIRO
        val pix = FormaPagamento.PIX
        val expectedMessageCartaoCredito = "Pagamento realizado com cartão de crédito."
        val expectedMessageCartaoDebito = "Pagamento realizado com cartão de débito."
        val expectedMessageDinheiro = "Pagamento realizado em dinheiro."
        val expectedMessagePix = "Pagamento realizado via PIX."

        //when
        val actualMessageCartaoCredito: String = pedidosService.metodoPagamento(cartaoCredito)
        val actualMessageCartaoDebito: String = pedidosService.metodoPagamento(cartaoDebito)
        val actualMessageDinheiro: String = pedidosService.metodoPagamento(dinheiro)
        val actualMessagePix: String = pedidosService.metodoPagamento(pix)

        //then
        assertThat(actualMessageCartaoCredito).isEqualTo(expectedMessageCartaoCredito)
        assertThat(actualMessageCartaoDebito).isEqualTo(expectedMessageCartaoDebito)
        assertThat(actualMessageDinheiro).isEqualTo(expectedMessageDinheiro)
        assertThat(actualMessagePix).isEqualTo(expectedMessagePix)
    }

    //Método para testar se os pedidos retornam pelo código de venda
    @Test
    fun `should return Pedidos by codVenda`() {
        val codVenda: String = Global.codVenda
        //given
        val fakePedidosList: List<Pedidos> = listOf(
            buildPedido(),
            buildPedido(),
            buildPedido(),
            buildPedido()
        )
        every { pedidosRepository.findByCodVenda(codVenda) } returns fakePedidosList

        //when
        val actual: List<Pedidos> = pedidosService.findByCodVenda(codVenda)

        //then
        verify { pedidosRepository.findByCodVenda(codVenda) }
        assertThat(actual).hasSize(4)
        assertThat(actual).containsExactlyInAnyOrderElementsOf(
            fakePedidosList.filter { it.codVenda == codVenda }
        )
    }

    @Test
    fun `should return list of all Pedidos`() {
        //given
        val fakePedidosList: List<Pedidos> = listOf(
            buildPedido(),
            buildPedido(codVenda = "cod123"),
            buildPedido(codVenda = "cod999")
        )
        every { pedidosRepository.findAll() } returns fakePedidosList

        //when
        val actualPedidosList: List<Pedidos> = pedidosService.findAll()

        //then
        verify { pedidosRepository.findAll() }
        assertThat(actualPedidosList).isEqualTo(fakePedidosList)
    }

    @Test
    fun `should return total value by codVenda`() {
        //given
        val codVenda = "cod123"
        val totalValue = BigDecimal("50.00")
        every { pedidosRepository.findTotalByCodVenda(codVenda) } returns totalValue

        //when
        val actualTotalValue: BigDecimal = pedidosService.findTotalByCodVenda(codVenda)

        //then
        verify { pedidosRepository.findTotalByCodVenda(codVenda) }
        assertThat(actualTotalValue).isEqualTo(totalValue)
    }

    private fun buildCarrinho(
        id: Long = 1L,
        idProduto: Long = 1L,
        nomeProduto: String = "X-salada",
        quantidadeProduto: Int = 3,
        precoProduto: BigDecimal = BigDecimal(5.50)
    ) = Carrinho(
        id = id,
        idProduto = idProduto,
        nomeProduto = nomeProduto,
        quantidadeProduto = quantidadeProduto,
        precoProduto = precoProduto
    )

    //Método privado para criar produtos que seram usados para o teste
    private fun buildProdutos(
        id: Long = 1L,
        nome: String = "X-salada",
        precoUnitario: BigDecimal = BigDecimal(6.50),
        quantidade: Int = 20,
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

    //Método privado para criar categorias que seram usados para o teste
    private fun buildCategoria(
        id: Long = 1L,
        nome: String = "Lanches"
    ) = Categoria(
        id = id,
        nome = nome
    )

    private fun buildPedido(
        nomeProduto: String = buildProdutos().nome,
        idProduto: Long? = buildProdutos().id,
        precoUnitario: BigDecimal = buildProdutos().precoUnitario,
        quantidade: Int = buildProdutos().quantidade,
        formaPagamento: FormaPagamento = FormaPagamento.CARTAO_CREDITO,
        codVenda: String = Global.codVenda
    )=  Pedidos(
        id = 1L,
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