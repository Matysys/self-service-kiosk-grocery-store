package tqi.autoatendimento.system.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tqi.autoatendimento.system.dto.FinalizacaoVendaDto
import tqi.autoatendimento.system.finalizacao.FinalizacaoVendaResponse
import tqi.autoatendimento.system.service.impl.FinalizacaoVendaService
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/finalizacao")
class FinalizacaoVendaController(private val finalizacaoVendaService: FinalizacaoVendaService) {

    @PostMapping()
    fun finalizarVenda(@RequestBody @Valid finalizacaoVendaDto: FinalizacaoVendaDto): ResponseEntity<FinalizacaoVendaResponse> {
        val valorTotal: BigDecimal = this.finalizacaoVendaService.finalizarVendaPreco(finalizacaoVendaDto.formaPagamento)
        val formaPagamento: String = this.finalizacaoVendaService.finalizarVendaPagamento(finalizacaoVendaDto.formaPagamento)
        val mensagem: String = "Obrigado(a) por comprar na JuMarket! Volte sempre!!!"

        val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val valorTotalFormatado: String = numberFormat.format(valorTotal)

        val response = FinalizacaoVendaResponse(formaPagamento, valorTotalFormatado, mensagem)
        return ResponseEntity.ok(response)
    }
}