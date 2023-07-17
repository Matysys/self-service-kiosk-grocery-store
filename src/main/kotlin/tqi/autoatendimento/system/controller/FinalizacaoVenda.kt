package tqi.autoatendimento.system.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tqi.autoatendimento.system.Dto.FinalizacaoVendaDto
import tqi.autoatendimento.system.service.impl.FinalizacaoVendaService
import java.math.BigDecimal

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/finalizacao")
class FinalizacaoVenda(private val finalizacaoVendaService: FinalizacaoVendaService) {

    @PostMapping()
    fun finalizarVenda(@RequestBody @Valid finalizacaoVendaDto: FinalizacaoVendaDto): ResponseEntity<String>{
        val valorTotal: BigDecimal = this.finalizacaoVendaService.finalizarVendaPreco()
        val formaPagamento: String = this.finalizacaoVendaService.finalizarVendaPagamento(finalizacaoVendaDto.formaPagamento)
        val response: String = "$formaPagamento\n\nO preço total foi de R$ $valorTotal\n\n\nObrigado(a) por comprar na JuMarket! Volte sempre!!!"
        return ResponseEntity.ok(response)
    }
}