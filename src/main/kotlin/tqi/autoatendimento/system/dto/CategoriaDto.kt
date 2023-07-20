package tqi.autoatendimento.system.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import tqi.autoatendimento.system.entity.Categoria

data class CategoriaDto(
    @field:NotEmpty
    @field:Size(max = 30)
    @field:Pattern(regexp = "[\\p{L}]+")
    val nome: String = ""
){
    fun toEntity(): Categoria = Categoria(
        nome = this.nome
    )
}
