package tqi.autoatendimento.system.service.impl

import org.springframework.stereotype.Service
import tqi.autoatendimento.system.entity.Pedidos
import tqi.autoatendimento.system.repository.PedidosRepository
import tqi.autoatendimento.system.service.IPedidosService

@Service
class PedidosService(private val pedidosRepository: PedidosRepository): IPedidosService {

    override fun save(pedidos: Pedidos){
        this.pedidosRepository.save(pedidos)
    }

}