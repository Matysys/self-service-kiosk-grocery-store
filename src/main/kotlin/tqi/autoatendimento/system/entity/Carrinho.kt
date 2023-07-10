package tqi.autoatendimento.system.entity

import jakarta.persistence.*

@Entity
@Table(name = "carrinho")
data class Carrinho(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, length = 20) val nomeProduto: String,
    @Column(nullable = false, length = 20) val quantidadeProduto: String,
    @Column(nullable = false, length = 20) val precoProduto: Int
)
