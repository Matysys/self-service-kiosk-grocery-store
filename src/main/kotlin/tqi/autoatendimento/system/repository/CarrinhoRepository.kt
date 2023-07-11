package tqi.autoatendimento.system.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import tqi.autoatendimento.system.entity.Carrinho
import java.math.BigDecimal

@Repository
interface CarrinhoRepository: JpaRepository<Carrinho, Long> {

    /*@Transactional
    @Modifying
    @Query(value = "INSERT INTO carrinho (nome_produto, quantidade_produto, preco_produto) VALUES (:nome, :quantidade, :precoProduto)", nativeQuery = true)
    fun saveCarrinho(@Param("nome") nome: String, @Param("quantidade") quantidade: Int, @Param("precoProduto") precoProduto: BigDecimal) */

    //Não utilizei na instrução JPA na instrução abaixo porque coloquei uma tabela adicional via Flyway Migrations para calcular os preços
    @Query(value = "SELECT * FROM carrinho", nativeQuery = true)
    fun findAllProdutos(): List<Carrinho>

    @Transactional
    @Modifying
    @Query(value = "UPDATE carrinho SET quantidade_produto = :quantidadeProduto, preco_produto = :precoProduto WHERE nome_produto = :nomeProduto", nativeQuery = true)
    fun update(@Param("nomeProduto") nomeProduto: String, @Param("quantidadeProduto") quantidadeProduto: Int, @Param("precoProduto") precoProduto: BigDecimal)

}