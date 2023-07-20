package tqi.autoatendimento.system.service

import tqi.autoatendimento.system.entity.Pedidos

interface IPedidosService {

    fun save(pedidos: Pedidos)
}