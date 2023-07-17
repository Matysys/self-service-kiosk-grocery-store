package tqi.autoatendimento.system.service.impl

import org.springframework.stereotype.Service
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.entity.Produtos
import tqi.autoatendimento.system.repository.ProdutosRepository
import tqi.autoatendimento.system.service.IProdutosService
import java.util.*

@Service
class ProdutosService(private val produtosRepository: ProdutosRepository): IProdutosService {

    override fun save(produtos: Produtos): Produtos {
        return this.produtosRepository.save(produtos)
    }

    override fun findAllByCategoria(categoria: String): List<Produtos> {
        return this.produtosRepository.findAllByCategoria(categoria)
    }

    override fun findById(id: Long): Optional<Produtos> {
        return this.produtosRepository.findById(id)
    }

    override fun findAllProdutos(): List<Produtos> {
        return this.produtosRepository.findAll()
    }

    override fun findAllProdutosByName(nome: String): List<Produtos> {
        return this.produtosRepository.findAllByNome(nome)
    }

    override fun editProdutos(produtos: Produtos): String {
        val status: Int = this.produtosRepository.editProdutos(produtos.id!!, produtos.nome, produtos.unidadeDeMedida, produtos.precoUnitario, produtos.categoria, produtos.quantidade)
        if(status == 1){
            return "O produto de ID: ${produtos.id} foi alterado com sucesso!"
        }else{
            return "Produto não encontrado."
        }
    }

    override fun delete(id: Long) {
        this.produtosRepository.deleteById(id)
    }
}