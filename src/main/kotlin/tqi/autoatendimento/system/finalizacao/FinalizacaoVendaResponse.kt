package tqi.autoatendimento.system.finalizacao

//Classe usada para retornar as informações para o cliente após a compra dos produtos.
data class FinalizacaoVendaResponse(
    val formaPagamento: String,
    val valorTotal: String,
    val codVenda: String,
    val mensagem: String
)
