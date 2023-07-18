package tqi.autoatendimento.system.finalizacao

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import tqi.autoatendimento.system.enum.FormaPagamento
import java.math.BigDecimal

data class FinalizacaoVenda(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val valorTotal: BigDecimal = BigDecimal.ZERO,
    val formaPagamento: FormaPagamento = FormaPagamento.CARTAO_CREDITO
)
