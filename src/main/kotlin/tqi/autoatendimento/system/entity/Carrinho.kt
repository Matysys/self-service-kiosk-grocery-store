package tqi.autoatendimento.system.entity

import jakarta.persistence.*
import java.math.BigDecimal

//Tabela do carrinho que será criada no banco de dados
@Entity
@Table(name = "carrinho")
data class Carrinho(
    //ID que é gerado automaticamente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false) val idProduto: Long,
    @Column(nullable = false, length = 20) val nomeProduto: String,
    @Column(nullable = false) val quantidadeProduto: Int,
    @Column(nullable = false) var precoProduto: BigDecimal = BigDecimal.ZERO,
)
