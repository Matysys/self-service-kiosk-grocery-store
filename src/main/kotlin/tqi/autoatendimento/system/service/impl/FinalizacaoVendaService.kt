package tqi.autoatendimento.system.service.impl

import org.springframework.stereotype.Service
import tqi.autoatendimento.system.entity.Carrinho
import tqi.autoatendimento.system.enum.FormaPagamento
import tqi.autoatendimento.system.repository.ProdutosRepository
import java.math.BigDecimal

@Service
class FinalizacaoVendaService(private val carrinhoService: CarrinhoService, private val produtosRepository: ProdutosRepository) {

    fun finalizarVendaPreco(): BigDecimal{
        val carrinho: List<Carrinho> = this.carrinhoService.findCarrinho()

        var valorTotal: BigDecimal = BigDecimal.ZERO

        for (item: Carrinho in carrinho) {
            valorTotal += item.precoProduto
            val a: Int = this.produtosRepository.removerEstoqueProduto(item.id!!, item.quantidadeProduto)
        }


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