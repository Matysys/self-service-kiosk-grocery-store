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
        carrinho.precoProduto = this.produtosRepository.calcularPreco(carrinho.nomeProduto, carrinho.quantidadeProduto)
        this.carrinhoRepository.save(carrinho)
    }

    override fun updateCarrinho(carrinho: Carrinho) {
        carrinho.precoProduto = this.produtosRepository.calcularPreco(carrinho.nomeProduto, carrinho.quantidadeProduto)
        this.carrinhoRepository.update(carrinho.nomeProduto, carrinho.quantidadeProduto, carrinho.precoProduto)
    }


    override fun findCarrinho(): List<Carrinho> {
        return this.carrinhoRepository.findAllProdutos()
    }

    override fun deleteById(id: Long) {
        this.carrinhoRepository.deleteById(id)
    }


    override fun deleteAll(){
        this.carrinhoRepository.deleteAll()
    }

}