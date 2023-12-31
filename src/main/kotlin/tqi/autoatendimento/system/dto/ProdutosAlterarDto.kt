package tqi.autoatendimento.system.dto

import jakarta.validation.constraints.*
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.entity.Produtos
import tqi.autoatendimento.system.enum.UnidadeMedida
import java.math.BigDecimal

//Informações que serão passadas via JSON para alterar um produto. Validações via Hibernate.
data class ProdutosAlterarDto(
    val id: Long,
    @field:NotEmpty
    @field:Size(max = 30)
    val nome: String = "",
    @field:NotNull
    val unidadeDeMedida: UnidadeMedida,
    @field:Min(0)
    val precoUnitario: BigDecimal,
    @field:Min(1)
    val categoriaId: Long,
    @field:Min(1)
    val quantidade: Int,
){
    fun toEntity(): Produtos = Produtos(
        id = this.id,
        nome = this.nome,
        unidadeDeMedida = this.unidadeDeMedida,
        precoUnitario = this.precoUnitario,
        categoria = Categoria(id = this.categoriaId),
        quantidade = this.quantidade
    )
}
