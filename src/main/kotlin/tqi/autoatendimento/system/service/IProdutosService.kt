package tqi.autoatendimento.system.service

import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.entity.Produtos
import java.util.*

interface IProdutosService {

    fun save(produtos: Produtos): Produtos

    fun findAllByCategoria(categoria: String): List<Produtos>

    fun findById(id: Long): Optional<Produtos>

    fun findAllProdutos(): List<Produtos>

    fun findAllProdutosByName(nome: String): List<Produtos>

    fun editProdutos(produto: Produtos): String

    fun delete(id: Long)
}