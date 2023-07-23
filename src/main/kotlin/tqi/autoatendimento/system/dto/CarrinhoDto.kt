package tqi.autoatendimento.system.dto

import jakarta.validation.constraints.*
import tqi.autoatendimento.system.entity.Carrinho
import tqi.autoatendimento.system.entity.Categoria

class CarrinhoDto (
    @field:NotEmpty
    @field:Size(max = 30)
    val nomeProduto: String = "",
    @field:Min(1)
    val idProduto: Long,
    @field:NotNull
    @field:Min(1)
    val quantidadeProduto: Int,
){
    fun toEntity(): Carrinho = Carrinho(
        idProduto = this.idProduto,
        nomeProduto = this.nomeProduto,
        quantidadeProduto = this.quantidadeProduto,
    )

}