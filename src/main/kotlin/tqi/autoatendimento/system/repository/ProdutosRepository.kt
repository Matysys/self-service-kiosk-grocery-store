package tqi.autoatendimento.system.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import tqi.autoatendimento.system.entity.Produtos

@Repository
interface ProdutosRepository: JpaRepository<Produtos, Long> {

    @Query(value = "SELECT * FROM produtos WHERE categoria = :categoria", nativeQuery = true)
    fun findAllByCategoria(@Param("categoria") categoria: String): List<Produtos>

    @Query(value = "SELECT * FROM produtos WHERE nome LIKE CONCAT('%', :nome, '%')", nativeQuery = true)
    fun findAllByNome(@Param("nome") nome: String): List<Produtos>

}