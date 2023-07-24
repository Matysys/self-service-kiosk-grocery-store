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

//Regras de negócio para os pedidos
@Service
class PedidosService(private val carrinhoService: CarrinhoService, private val produtosRepository: ProdutosRepository, private val pedidosRepository: PedidosRepository): IPedidosService {

    //Finalizar o pedido e retorna o Array com o valor total e código de venda
    override fun finalizarPedido(pagamento: FormaPagamento): Array<Any>{
        //Lista o carrinho e guarda na variável
        val carrinho: List<Carrinho> = this.carrinhoService.findCarrinho()

        //Valor total iniciado por 0
        var valorTotal: BigDecimal = BigDecimal.ZERO

        //Código de venda fixo para esse pedido
        val codVenda: UUID = UUID.randomUUID()

        //Uma iteração para cada item da lista de carrinhos
        for (item: Carrinho in carrinho) {

            //Cria o pedido com cada item
            val pedido = Pedidos(
                nomeProduto = item.nomeProduto,
                idProduto = item.idProduto,
                precoUnitario = item.precoProduto,
                quantidade = item.quantidadeProduto,
                formaPagamento = pagamento,
                codVenda = codVenda.toString()
            )

            //Adiciona o valor de cada item do carrinho no valor total
            valorTotal += item.precoProduto

            //Remove o estoque do produto a cada iteração
            this.produtosRepository.removerEstoqueProduto(item.idProduto, item.quantidadeProduto)

            //Salva cada pedido a cada iteração
            this.pedidosRepository.save(pedido)
        }
        //Faz o TRUNCATE na tabela e apaga de vez o carrinho
        this.carrinhoService.truncateAll()
        //Retorna o array com o valor total e código de venda para a camada de controle
        return arrayOf(valorTotal, codVenda.toString())
    }

    //Retorna a mensagem de acordo com o método de pagamento escolhido
    override fun metodoPagamento(pagamento: FormaPagamento): String{
        return when (pagamento){
            FormaPagamento.CARTAO_CREDITO -> "Pagamento realizado com cartão de crédito."
            FormaPagamento.CARTAO_DEBITO -> "Pagamento realizado com cartão de débito."
            FormaPagamento.DINHEIRO -> "Pagamento realizado em dinheiro."
            FormaPagamento.PIX -> "Pagamento realizado via PIX."
        }
    }

    //Retorna todos os pedidos pelo código de venda
    override fun findByCodVenda(cod: String): List<Pedidos> {
        return this.pedidosRepository.findByCodVenda(cod)
    }

    //Retorna todos os pedidos
    override fun findAll(): List<Pedidos>{
        return this.pedidosRepository.findAll()
    }

    //Retorna o valor total dos pedidos pelo código de venda
    override fun findTotalByCodVenda(cod: String): BigDecimal {
        return this.pedidosRepository.findTotalByCodVenda(cod)
    }
}