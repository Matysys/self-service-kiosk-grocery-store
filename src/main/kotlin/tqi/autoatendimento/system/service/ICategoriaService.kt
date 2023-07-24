package tqi.autoatendimento.system.service

import tqi.autoatendimento.system.entity.Categoria

//Implementações
interface ICategoriaService {
    fun save(categoria: Categoria): Categoria
    fun findById(id: Long): Categoria
    fun findAllCategoria(): List<Categoria>
    fun deleteById(id: Long)
    fun deleteAll()
}