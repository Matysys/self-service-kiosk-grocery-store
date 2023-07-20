package tqi.autoatendimento.system.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import tqi.autoatendimento.system.finalizacao.FinalizacaoVenda
import tqi.autoatendimento.system.enum.FormaPagamento

class FinalizacaoVendaDto (
    @field:NotNull
    val formaPagamento: FormaPagamento
)


