package tqi.autoatendimento.system.entity

import jakarta.persistence.*
import tqi.autoatendimento.system.enum.UnidadeMedida
import java.math.BigDecimal

//Tabela dos produtos que será criada no banco de dados
@Entity
@Table(name = "produtos")
data class Produtos(
    //ID será gerado automaticamente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, length = 30) val nome: String,
    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    val unidadeDeMedida: UnidadeMedida,
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)") val precoUnitario: BigDecimal,
    @Column(nullable = false) val quantidade: Int,
    //Não estava na imagem do desafio, mas eu acredito que é necessário atribuir uma categoria para o produto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    val categoria: Categoria = Categoria()
)
