package tqi.autoatendimento.system.service

import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.entity.Produtos

interface IProdutosService {

    fun save(produtos: Produtos): Produtos

    fun findAllByCategoria(categoria: Categoria): List<Produtos>

    fun findById(id: Long): Produtos

    fun delete(id: Long): Produtos
}