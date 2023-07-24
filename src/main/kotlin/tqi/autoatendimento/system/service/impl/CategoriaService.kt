package tqi.autoatendimento.system.service.impl

import org.springframework.stereotype.Service
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.repository.CategoriaRepository
import tqi.autoatendimento.system.service.ICategoriaService

//Regras de negócio para a categoria
@Service
class CategoriaService(private val categoriaRepository: CategoriaRepository): ICategoriaService {

    //Salva uma nova categoria
    override fun save(categoria: Categoria): Categoria =
        this.categoriaRepository.save(categoria)

    //Encontra uma categoria pelo ID
    override fun findById(id: Long): Categoria =
        this.categoriaRepository.findById(id).orElseThrow{
            throw RuntimeException("Id $id não encontrado.")
        }

    //Retorna todas as categorias
    override fun findAllCategoria(): List<Categoria> =
        this.categoriaRepository.findAll()

    //Deleta uma categoria pelo ID
    override fun deleteById(id: Long) {
        this.categoriaRepository.deleteById(id)
    }

    //Deleta todas as categorias
    override fun deleteAll() {
       this.categoriaRepository.deleteAll()
    }
}