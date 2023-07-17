package tqi.autoatendimento.system.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import tqi.autoatendimento.system.entity.Produtos
import java.math.BigDecimal

@Repository
interface ProdutosRepository: JpaRepository<Produtos, Long> {

    @Query(value = "SELECT * FROM produtos WHERE categoria = :categoria", nativeQuery = true)
    fun findAllByCategoria(@Param("categoria") categoria: String): List<Produtos>

    @Query(value = "SELECT * FROM produtos WHERE nome LIKE CONCAT('%', :nome, '%')", nativeQuery = true)
    fun findAllByNome(@Param("nome") nome: String): List<Produtos>

    @Query(value = "SELECT SUM(preco_unitario * :quantidade) as preco_total FROM produtos WHERE nome = :nome", nativeQuery = true)
    fun calcularPreco(@Param("nome") nome: String, @Param("quantidade") quantidade: Int): BigDecimal

    @Transactional
    @Modifying
    @Query(value = "UPDATE produtos SET nome = :nome, unidade_de_medida = :unidadeDeMedida, " +
            "preco_unitario = :precoUnitario, categoria = :categoria, quantidade = :quantidade WHERE id = :id", nativeQuery = true)
    fun editProdutos(@Param("id") id: Long,
                     @Param("nome") nome: String,
                     @Param("unidadeDeMedida") unidadeDeMedida: String,
                     @Param("precoUnitario") precoUnitario: BigDecimal,
                     @Param("categoria") categoria: String,
                     @Param("quantidade") quantidade: Int): Int

}