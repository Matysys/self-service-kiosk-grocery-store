package tqi.autoatendimento.system.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "produtos")
data class Produtos(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, length = 30) val nome: String,
    @Column(nullable = false, length = 10) val unidadeDeMedida: String,
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)") val precoUnitario: BigDecimal,
    //Não estava na imagem do desafio, mas eu acredito que é necessário atribuir uma categoria para o produto.
    @Column(nullable = false, length = 30) val categoria: String,
    @Column(nullable = false) val quantidade: Int,
)
