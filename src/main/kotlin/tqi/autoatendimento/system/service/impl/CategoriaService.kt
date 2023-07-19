package tqi.autoatendimento.system.service.impl

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.repository.CategoriaRepository
import tqi.autoatendimento.system.service.ICategoriaService

@Service
class CategoriaService(private val categoriaRepository: CategoriaRepository): ICategoriaService {
    override fun save(categoria: Categoria): Categoria =
        this.categoriaRepository.save(categoria)

    override fun findById(id: Long): Categoria =
        this.categoriaRepository.findById(id).orElseThrow{
            throw RuntimeException("Id $id não encontrado.")
        }

    override fun findAllCategoria(): List<Categoria> =
        this.categoriaRepository.findAll()

    override fun deleteById(id: Long) {
        this.categoriaRepository.deleteById(id)
    }

    override fun deleteAll() {
       this.categoriaRepository.deleteAll()
    }

}