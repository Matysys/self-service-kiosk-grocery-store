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
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/pedidos")
class PedidosController(private val pedidoService: PedidosService) {

    //Checkout, finaliza a venda
    @PostMapping()
    fun finalizarVenda(@RequestBody @Valid finalizacaoVendaDto: FinalizacaoVendaDto): ResponseEntity<FinalizacaoVendaResponse> {

        //Recebe um array que contém o código de venda e o valor total
        val valor: Array<Any> = this.pedidoService.finalizarPedido(finalizacaoVendaDto.formaPagamento)
        //Recebe a mensagem que será retornada para o(a) cliente sobre a forma de pagamento escolhida
        val formaPagamento: String = this.pedidoService.metodoPagamento(finalizacaoVendaDto.formaPagamento)

        //Instrução de como proceder ao fazer a compra
        val mensagem: String = "Obrigado(a) por comprar na JuMarket! Volte sempre!!! Leve o papel que contém o código de venda para obter seus produtos no balcão!"

        //Formatar o valor total para reais
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val valorTotalFormatado: String = numberFormat.format(valor[0])

        //Resposta retornada contendo o valor total, mensagem da forma de pagamento, código de venda e instrução
        val response = FinalizacaoVendaResponse(formaPagamento, valorTotalFormatado, valor[1].toString(), mensagem)
        return ResponseEntity.ok(response)
    }

    //Retorna todos os pedidos pelo código de venda
    @GetMapping()
    fun findByCodVenda(@RequestParam(required = false, defaultValue = "") cod: String,
                       @RequestParam(required = false, defaultValue = "false") total: Boolean): ResponseEntity<out Any> {

        //Se o código de venda não for vazio.
        if(cod.isNotEmpty()){
            //Verifica se o parâmetro 'total' é falso
            if(!total) {
                //Retorna todos os pedidos pelo código de venda se o 'total' for falso
                val venda: List<Pedidos> = this.pedidoService.findByCodVenda(cod)
                return ResponseEntity.ok().body(venda)
            }else{
                //Se o total for verdadeiro, retorna somente o valor total dos pedidos pelo código de venda, formatado
                val numberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
                val venda: BigDecimal = this.pedidoService.findTotalByCodVenda(cod)
                return ResponseEntity.ok().body(numberFormat.format(venda))
            }
        }else{
            //Se nenhuma condição for verdadeira, apenas retorna todos os pedidos com todos os códigos de venda
            val venda: List<Pedidos> = this.pedidoService.findAll()
            return ResponseEntity.ok().body(venda)
        }
    }
}