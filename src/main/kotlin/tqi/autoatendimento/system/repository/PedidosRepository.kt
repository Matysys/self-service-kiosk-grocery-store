package tqi.autoatendimento.system.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import tqi.autoatendimento.system.entity.Pedidos
import java.math.BigDecimal

//Queries para a tabela 'pedidos'
@Repository
interface PedidosRepository: JpaRepository<Pedidos, Long> {

    //SELECIONA todos os campos dos pedidos de acordo com o código de venda
    @Query(value = "SELECT * FROM pedidos WHERE cod_venda = :cod", nativeQuery = true)
    fun findByCodVenda(@Param("cod") cod: String): List<Pedidos>

    //Retorna o valor total dos pedidos de acordo com o código de venda
    @Query(value = "SELECT SUM(preco_unitario) as preco_total FROM pedidos WHERE cod_venda = :cod", nativeQuery = true)
    fun findTotalByCodVenda(@Param("cod") cod: String): BigDecimal
}