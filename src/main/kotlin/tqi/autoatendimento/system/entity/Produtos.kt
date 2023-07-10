package tqi.autoatendimento.system.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "produtos")
data class Produtos(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, length = 20) val unidadeDeMedida: String,
    @Column(nullable = false, length = 20) val precoUnitario: BigDecimal,
)
