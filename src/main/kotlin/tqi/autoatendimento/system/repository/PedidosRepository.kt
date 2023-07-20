package tqi.autoatendimento.system.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.entity.Pedidos

@Repository
interface PedidosRepository: JpaRepository<Pedidos, Long> {



}