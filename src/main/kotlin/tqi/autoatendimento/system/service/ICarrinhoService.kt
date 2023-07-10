package tqi.autoatendimento.system.service

import tqi.autoatendimento.system.entity.Carrinho

interface ICarrinhoService {

    fun save(carrinho: Carrinho): Carrinho

    fun findCarrinho(carrinho: Carrinho): Carrinho

    fun delete(carrinho: Carrinho): Carrinho
}