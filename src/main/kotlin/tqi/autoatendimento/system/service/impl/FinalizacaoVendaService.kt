package tqi.autoatendimento.system.service.impl

import org.springframework.stereotype.Service
import tqi.autoatendimento.system.entity.Carrinho
import tqi.autoatendimento.system.entity.Pedidos
import tqi.autoatendimento.system.enum.FormaPagamento
import tqi.autoatendimento.system.repository.ProdutosRepository
import java.math.BigDecimal
import java.util.UUID

@Service
class FinalizacaoVendaService(private val carrinhoService: CarrinhoService, private val produtosRepository: ProdutosRepository, private val pedidosService: PedidosService) {

    fun finalizarVendaPreco(pagamento: FormaPagamento): BigDecimal{
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
            this.pedidosService.save(pedido)
        }

        this.carrinhoService.deleteAll()


        return valorTotal

    }

    fun finalizarVendaPagamento(pagamento: FormaPagamento): String{
        return when (pagamento){
            FormaPagamento.CARTAO_CREDITO -> "Pagamento realizado com cartão de crédito."
            FormaPagamento.CARTAO_DEBITO -> "Pagamento realizado com cartão de débito."
            FormaPagamento.DINHEIRO -> "Pagamento realizado em dinheiro."
            FormaPagamento.PIX -> "Pagamento realizado via PIX."
        }
    }

}