package tqi.autoatendimento.system.entity

import java.math.BigDecimal

data class FinalizacaoVenda(
    val id: Long,
    val valorTotal: BigDecimal,
    val formaPagamento: FormaPagamento,
)
