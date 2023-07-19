package tqi.autoatendimento.system.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.entity.Produtos
import tqi.autoatendimento.system.enum.UnidadeMedida
import java.math.BigDecimal

data class ProdutosAlterarDto(
    @NotBlank
    val id: Long,
    @NotBlank
    @Size(max = 30)
    val nome: String = "",
    @NotBlank
    val unidadeDeMedida: UnidadeMedida,
    @NotBlank
    val precoUnitario: BigDecimal,
    @NotBlank
    @Size(max = 30)
    val categoriaId: Long,
    @NotBlank
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