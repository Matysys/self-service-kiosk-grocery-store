package tqi.autoatendimento.system.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import tqi.autoatendimento.system.entity.Produtos
import java.math.BigDecimal

//Queries para a tabela 'produtos'
@Repository
interface ProdutosRepository: JpaRepository<Produtos, Long> {

    //SELECIONA todos os campos dos produtos de acordo com o ID da categoria
    @Query(value = "SELECT * FROM produtos WHERE categoria_id = :categoria", nativeQuery = true)
    fun findAllByCategoria(@Param("categoria") categoria: Long): List<Produtos>

    //SELECIONA todos os campos dos produtos que contém o nome enviado
    @Query(value = "SELECT * FROM produtos WHERE nome LIKE CONCAT('%', :nome, '%')", nativeQuery = true)
    fun findAllByNome(@Param("nome") nome: String): List<Produtos>

    //Calcula o preco total dos produtos de acordo com a quantidade
    @Query(value = "SELECT SUM(preco_unitario * :quantidade) as preco_total FROM produtos WHERE id = :id AND nome = :nome", nativeQuery = true)
    fun calcularPreco(@Param("id") id: Long, @Param("nome") nome: String, @Param("quantidade") quantidade: Int): BigDecimal

    //Altera um produto
    @Transactional
    @Modifying
    @Query(value = "UPDATE produtos SET nome = :nome, unidade_de_medida = :unidadeDeMedida, " +
            "preco_unitario = :precoUnitario, categoria_id = :categoria, quantidade = :quantidade WHERE id = :id", nativeQuery = true)
    fun editProdutos(@Param("id") id: Long,
                     @Param("nome") nome: String,
                     @Param("unidadeDeMedida") unidadeDeMedida: String,
                     @Param("precoUnitario") precoUnitario: BigDecimal,
                     @Param("categoria") categoria: Long,
                     @Param("quantidade") quantidade: Int): Int

    //Verifica a quantidade de produtos pelo ID
    @Query(value = "SELECT quantidade FROM produtos WHERE id = :id", nativeQuery = true)
    fun verificarQntProdutos(@Param("id") id: Long): Int

    //Remove o estoque dos produtos a cada compra finalizada
    @Transactional
    @Modifying
    @Query(value = "UPDATE produtos SET quantidade = quantidade - :quantidade WHERE id = :id", nativeQuery = true)
    fun removerEstoqueProduto(@Param("id") id: Long, @Param("quantidade") quantidade: Int)

}