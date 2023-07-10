package tqi.autoatendimento.system.service.impl

import org.springframework.stereotype.Service
import tqi.autoatendimento.system.entity.Carrinho
import tqi.autoatendimento.system.repository.CarrinhoRepository
import tqi.autoatendimento.system.repository.ProdutosRepository
import tqi.autoatendimento.system.service.ICarrinhoService
import java.math.BigDecimal

@Service
class CarrinhoService(private val carrinhoRepository: CarrinhoRepository
, private val produtosRepository: ProdutosRepository): ICarrinhoService {
    override fun saveCarrinho(carrinho: Carrinho) {
        val valorProdutos: BigDecimal = this.produtosRepository.calcularPreco(carrinho.nomeProduto, carrinho.quantidadeProduto)
        return this.carrinhoRepository.saveCarrinho(carrinho.nomeProduto, carrinho.quantidadeProduto, valorProdutos)
    }

    override fun findCarrinho(): List<Carrinho> {
        return this.carrinhoRepository.findAll()
    }

    override fun delete(carrinho: Carrinho): Carrinho {
        TODO("Not yet implemented")
    }

}