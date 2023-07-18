package tqi.autoatendimento.system.service

import tqi.autoatendimento.system.entity.Carrinho

interface ICarrinhoService {

    fun saveCarrinho(carrinho: Carrinho): String

    fun updateCarrinho(carrinho: Carrinho)

    fun findCarrinho(): List<Carrinho>

    fun deleteById(id: Long)

    fun deleteAll()
}