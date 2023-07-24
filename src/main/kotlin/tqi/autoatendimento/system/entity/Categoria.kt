package tqi.autoatendimento.system.entity

import jakarta.persistence.*

//Tabela das categorias que será criada no banco de dados
@Entity
@Table(name = "categoria")
data class Categoria(
    //ID gerado automaticamente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, length = 20) val nome: String = "",

)
