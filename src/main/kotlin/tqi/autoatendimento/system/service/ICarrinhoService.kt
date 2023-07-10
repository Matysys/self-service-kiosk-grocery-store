package tqi.autoatendimento.system.service

import tqi.autoatendimento.system.entity.Carrinho

interface ICarrinhoService {

    fun saveCarrinho(carrinho: Carrinho)

    fun findCarrinho(): List<Carrinho>

    fun delete(carrinho: Carrinho): Carrinho
}