package tqi.autoatendimento.system.dto

import jakarta.validation.constraints.NotNull
import tqi.autoatendimento.system.enum.FormaPagamento

//Informação que será passada via JSON para concluir o pedido. Validação via Hibernate
class FinalizacaoVendaDto (
    @field:NotNull
    val formaPagamento: FormaPagamento
)


