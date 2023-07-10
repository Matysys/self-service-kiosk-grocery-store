package tqi.autoatendimento.system.entity

import jakarta.persistence.*

@Entity
@Table(name = "categoria")
data class Categoria(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, length = 20) val nome: String

)
