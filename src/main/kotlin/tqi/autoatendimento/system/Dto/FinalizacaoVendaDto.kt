package tqi.autoatendimento.system.Dto

import jakarta.validation.constraints.NotBlank
import tqi.autoatendimento.system.finalizacao.FinalizacaoVenda
import tqi.autoatendimento.system.enum.FormaPagamento

class FinalizacaoVendaDto (
    @NotBlank
    val formaPagamento: FormaPagamento,
){
    fun toEntity(): FinalizacaoVenda = FinalizacaoVenda(
        formaPagamento = this.formaPagamento
    )

}