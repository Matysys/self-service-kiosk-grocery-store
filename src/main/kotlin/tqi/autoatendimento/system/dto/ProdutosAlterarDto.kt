package tqi.autoatendimento.system.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.entity.Produtos
import tqi.autoatendimento.system.enum.UnidadeMedida
import java.math.BigDecimal

data class ProdutosAlterarDto(
    val id: Long,
    @field:NotEmpty
    @Size(max = 30)
    val nome: String = "",
    @field:NotNull
    val unidadeDeMedida: UnidadeMedida,
    @field:Min(0)
    val precoUnitario: BigDecimal,
    @Size(max = 30)
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
