package tqi.autoatendimento.system.service

import tqi.autoatendimento.system.entity.Pedidos
import tqi.autoatendimento.system.enum.FormaPagamento

interface IPedidosService {

    fun finalizarPedido(pagamento: FormaPagamento): Array<Any>

    fun metodoPagamento(pagamento: FormaPagamento): String

    fun findByCodVenda(cod: String): List<Pedidos>

}