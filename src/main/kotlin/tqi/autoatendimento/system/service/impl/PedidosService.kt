package tqi.autoatendimento.system.service.impl

import org.springframework.stereotype.Service
import tqi.autoatendimento.system.entity.Carrinho
import tqi.autoatendimento.system.entity.Pedidos
import tqi.autoatendimento.system.enum.FormaPagamento
import tqi.autoatendimento.system.repository.PedidosRepository
import tqi.autoatendimento.system.repository.ProdutosRepository
import tqi.autoatendimento.system.service.IPedidosService
import java.math.BigDecimal
import java.util.*

@Service
class PedidosService(private val carrinhoService: CarrinhoService, private val produtosRepository: ProdutosRepository, private val pedidosRepository: PedidosRepository): IPedidosService {

    override fun finalizarPedido(pagamento: FormaPagamento): Array<Any>{
        val carrinho: List<Carrinho> = this.carrinhoService.findCarrinho()

        var valorTotal: BigDecimal = BigDecimal.ZERO
        val codVenda: UUID = UUID.randomUUID()


        for (item: Carrinho in carrinho) {

            val pedido: Pedidos = Pedidos(
                nomeProduto = item.nomeProduto,
                idProduto = item.idProduto,
                precoUnitario = item.precoProduto,
                quantidade = item.quantidadeProduto,
                formaPagamento = pagamento,
                codVenda = codVenda.toString()
            )

            valorTotal += item.precoProduto
            this.produtosRepository.removerEstoqueProduto(item.idProduto, item.quantidadeProduto)
            this.pedidosRepository.save(pedido)
        }

        this.carrinhoService.truncateAll()


        return arrayOf(valorTotal, codVenda.toString())

    }

    override fun metodoPagamento(pagamento: FormaPagamento): String{
        return when (pagamento){
            FormaPagamento.CARTAO_CREDITO -> "Pagamento realizado com cartão de crédito."
            FormaPagamento.CARTAO_DEBITO -> "Pagamento realizado com cartão de débito."
            FormaPagamento.DINHEIRO -> "Pagamento realizado em dinheiro."
            FormaPagamento.PIX -> "Pagamento realizado via PIX."
        }
    }

    override fun findByCodVenda(cod: String): List<Pedidos> {
        return this.pedidosRepository.findByCodVenda(cod)
    }

    override fun findAll(): List<Pedidos>{
        return this.pedidosRepository.findAll()
    }

    override fun findTotalByCodVenda(cod: String): BigDecimal {
        return this.pedidosRepository.findTotalByCodVenda(cod)
    }


}