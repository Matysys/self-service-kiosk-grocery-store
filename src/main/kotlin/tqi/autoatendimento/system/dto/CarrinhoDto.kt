package tqi.autoatendimento.system.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import tqi.autoatendimento.system.entity.Carrinho

class CarrinhoDto (
    @field:NotEmpty
    @Size(max = 30)
    val nomeProduto: String = "",
    @field:Min(1)
    val idProduto: Long,
    @field:NotEmpty
    @Size(max = 30)
    @field:Min(1)
    val quantidadeProduto: Int
){
    fun toEntity(): Carrinho = Carrinho(
        idProduto = this.idProduto,
        nomeProduto = this.nomeProduto,
        quantidadeProduto = this.quantidadeProduto
    )

}