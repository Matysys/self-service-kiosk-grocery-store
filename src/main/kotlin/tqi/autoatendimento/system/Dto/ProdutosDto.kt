package tqi.autoatendimento.system.Dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import tqi.autoatendimento.system.entity.Produtos
import java.math.BigDecimal

data class ProdutosDto(
    @NotBlank
    @Size(max = 30)
    val nome: String = "",
    @NotBlank
    val unidadeDeMedida: String = "",
    @NotBlank
    val precoUnitario: BigDecimal,
    @NotBlank
    @Size(max = 30)
    val categoria: String = "",
    @NotBlank
    val quantidade: Int


){
    fun toEntity(): Produtos = Produtos(
        nome = this.nome,
        unidadeDeMedida = this.unidadeDeMedida,
        precoUnitario = this.precoUnitario,
        categoria = this.categoria,
        quantidade = this.quantidade
    )
}
