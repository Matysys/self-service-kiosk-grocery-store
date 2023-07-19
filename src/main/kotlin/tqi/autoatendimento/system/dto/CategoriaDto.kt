package tqi.autoatendimento.system.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import tqi.autoatendimento.system.entity.Categoria

data class CategoriaDto(
    @NotBlank
    @Size(max = 30)
    val nome: String = ""
){
    fun toEntity(): Categoria = Categoria(
        nome = this.nome
    )
}
