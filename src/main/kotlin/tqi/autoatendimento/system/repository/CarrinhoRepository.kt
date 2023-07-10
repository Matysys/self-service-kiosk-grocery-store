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

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO carrinho (nome_produto, quantidade_produto, preco_produto) VALUES (:nome, :quantidade, :precoProduto)", nativeQuery = true)
    fun saveCarrinho(@Param("nome") nome: String, @Param("quantidade") quantidade: Int, @Param("precoProduto") precoProduto: BigDecimal)

}