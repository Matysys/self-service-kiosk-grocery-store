package tqi.autoatendimento.system.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tqi.autoatendimento.system.dto.FinalizacaoVendaDto
import tqi.autoatendimento.system.entity.Pedidos
import tqi.autoatendimento.system.finalizacao.FinalizacaoVendaResponse
import tqi.autoatendimento.system.service.impl.PedidosService
import java.text.NumberFormat
import java.util.*

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/pedidos")
class PedidosController(private val pedidoService: PedidosService) {

    @PostMapping()
    fun finalizarVenda(@RequestBody @Valid finalizacaoVendaDto: FinalizacaoVendaDto): ResponseEntity<FinalizacaoVendaResponse> {
        val valor: Array<Any> = this.pedidoService.finalizarPedido(finalizacaoVendaDto.formaPagamento)
        val formaPagamento: String = this.pedidoService.metodoPagamento(finalizacaoVendaDto.formaPagamento)
        val mensagem: String = "Obrigado(a) por comprar na JuMarket! Volte sempre!!! Leve o papel que contém o código de venda para obter seus produtos no balcão!"

        val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val valorTotalFormatado: String = numberFormat.format(valor[0])

        val response = FinalizacaoVendaResponse(formaPagamento, valorTotalFormatado, valor[1].toString(), mensagem)
        return ResponseEntity.ok(response)
    }

    @GetMapping()
    fun findByCodVenda(@RequestParam(required = false, defaultValue = "") cod: String): ResponseEntity<List<Pedidos>>{
        val venda: List<Pedidos> = this.pedidoService.findByCodVenda(cod)
        return ResponseEntity.ok().body(venda)
    }
}