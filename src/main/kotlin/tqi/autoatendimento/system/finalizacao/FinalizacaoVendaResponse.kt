package tqi.autoatendimento.system.finalizacao

import java.math.BigDecimal

data class FinalizacaoVendaResponse(
    val formaPagamento: String,
    val valorTotal: String, //Para poder mostrar o cifrão na resposta JSON.
    val mensagem: String
)
