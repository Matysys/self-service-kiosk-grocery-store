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

    //ssss
    override fun saveCarrinho(carrinho: Carrinho): String {
        carrinho.precoProduto = this.produtosRepository.calcularPreco(carrinho.idProduto, carrinho.nomeProduto, carrinho.quantidadeProduto)
        val quantidadeAtual: Int = this.produtosRepository.verificarQntProdutos(carrinho.idProduto)

        if (carrinho.quantidadeProduto <= quantidadeAtual) {
            val exist: Long? = this.carrinhoRepository.existsCarrinho(carrinho.idProduto)
            if (exist != null) {
                return "Produto já está no carrinho."
            } else {
                this.carrinhoRepository.save(carrinho)
                return "O carrinho foi atualizado com sucesso."
            }
        } else {
            return "Não há estoque o suficiente para a quantidade solicitada."
        }

    }

    override fun updateCarrinho(carrinho: Carrinho) {
        carrinho.precoProduto = this.produtosRepository.calcularPreco(carrinho.idProduto, carrinho.nomeProduto, carrinho.quantidadeProduto)
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

    override fun truncateAll(){
        this.carrinhoRepository.truncateAll()
    }

}