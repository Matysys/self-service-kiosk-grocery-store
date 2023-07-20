package tqi.autoatendimento.system.finalizacao

import java.math.BigDecimal

data class FinalizacaoVendaResponse(
    val formaPagamento: String,
    val valorTotal: String,
    val codVenda: String,
    val mensagem: String
)
