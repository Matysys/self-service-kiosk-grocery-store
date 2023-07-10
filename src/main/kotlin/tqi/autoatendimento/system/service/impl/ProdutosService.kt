package tqi.autoatendimento.system.service.impl

import org.springframework.stereotype.Service
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.entity.Produtos
import tqi.autoatendimento.system.repository.ProdutosRepository
import tqi.autoatendimento.system.service.IProdutosService

@Service
class ProdutosService(private val produtosRepository: ProdutosRepository): IProdutosService {

    override fun save(produtos: Produtos): Produtos {
        TODO("Not yet implemented")
    }

    override fun findAllByCategoria(categoria: Categoria): List<Produtos> {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): Produtos {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long): Produtos {
        TODO("Not yet implemented")
    }
}