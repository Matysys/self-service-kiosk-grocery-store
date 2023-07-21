package tqi.autoatendimento.system.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import tqi.autoatendimento.system.entity.Carrinho
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.entity.Pedidos
import tqi.autoatendimento.system.entity.Produtos
import tqi.autoatendimento.system.enum.FormaPagamento
import tqi.autoatendimento.system.enum.UnidadeMedida
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PedidosRepositoryTest {
    @Autowired lateinit var pedidosRepository: PedidosRepository
    @Autowired lateinit var testEntityManager: TestEntityManager

    private lateinit var pedidos: Pedidos
    private lateinit var pedidos2: Pedidos

    @BeforeEach fun setup(){
        pedidos = testEntityManager.persist(buildPedidos())
        pedidos2 = testEntityManager.persist(buildPedidos(nomeProduto = "X-tudo", quantidade = 3))
    }

    @AfterEach
    fun teardown(){
        pedidosRepository.deleteAll()
    }

    @Test
    fun `should find pedidos by codVenda`() {
        //given
        val codVenda: String = pedidos.codVenda

        //when
        val pedidosEncontrados: List<Pedidos> = pedidosRepository.findByCodVenda(codVenda)

        //then
        assertThat(pedidosEncontrados).isNotEmpty
        assertThat(pedidosEncontrados).hasSize(2)
        assertThat(pedidosEncontrados).contains(pedidos, pedidos2)
    }

    @Test
    fun `should find total value by codVenda`() {
        //given
        val codVenda: String = pedidos.codVenda
        val expectedTotalValue = pedidos.precoUnitario + pedidos2.precoUnitario

        //when
        val totalValue: BigDecimal = pedidosRepository.findTotalByCodVenda(codVenda)

        //then
        assertThat(totalValue).isEqualTo(expectedTotalValue.setScale(2, RoundingMode.HALF_EVEN))
    }

    private fun buildPedidos(
        nomeProduto: String = "X-salada",
        precoUnitario: BigDecimal = BigDecimal(5.5),
        quantidade: Int = 8,
        formaPagamento: FormaPagamento = FormaPagamento.PIX,
        codVenda: String = Global.codVenda
    ) = Pedidos(
        idProduto = 1,
        nomeProduto = nomeProduto,
        precoUnitario = precoUnitario,
        formaPagamento = formaPagamento,
        quantidade = quantidade,
        codVenda = codVenda
    )

}

object Global {
    var codVenda: String = UUID.randomUUID().toString()
}