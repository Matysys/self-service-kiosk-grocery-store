package tqi.autoatendimento.system.service

import tqi.autoatendimento.system.entity.Produtos
import java.util.*

//Implementações
interface IProdutosService {
    fun save(produtos: Produtos): Produtos
    fun findAllByCategoria(categoriaId: Long): List<Produtos>
    fun findById(id: Long): Optional<Produtos>
    fun findAllProdutos(): List<Produtos>
    fun findAllProdutosByName(nome: String): List<Produtos>
    fun editProdutos(produtos: Produtos): String
    fun delete(id: Long)
}