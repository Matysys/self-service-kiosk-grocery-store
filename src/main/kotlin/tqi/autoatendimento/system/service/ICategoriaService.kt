package tqi.autoatendimento.system.service

import tqi.autoatendimento.system.entity.Categoria

interface ICategoriaService {

    fun save(categoria: Categoria): Categoria

    fun findById(id: Long): Categoria

    fun findAllCategoria(categoria: Categoria): List<Categoria>

}