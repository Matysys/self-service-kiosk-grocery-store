package tqi.autoatendimento.system.controller

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tqi.autoatendimento.system.Dto.CategoriaDto
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.service.impl.CategoriaService
import java.util.*

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/categoria")
class CategoriaController(private val categoriaService: CategoriaService) {

    @PostMapping
    fun saveCategoria(@RequestBody @Valid categoriaDto: CategoriaDto): String{
        val categoria = this.categoriaService.save(categoriaDto.toEntity())
        return "A categoria '${categoria.nome}' foi adicionada com sucesso!"
    }

    @GetMapping("/{id}")
    fun getCategoriaById(@PathVariable id: Long): Optional<Categoria> {
        val categoria: Optional<Categoria> = Optional.of(this.categoriaService.findById(id))
        return categoria
    }

    @GetMapping
    fun getAllCategoria(): Optional<List<Categoria>> {
        val categorias: List<Categoria> = this.categoriaService.findAllCategoria()
        return Optional.of(categorias)
    }

    @DeleteMapping("/{id}")
    fun deleteCategoriaById(@PathVariable id: Long): String{
        this.categoriaService.deleteById(id)
        return "Categoria de número $id deletada com sucesso"
    }

    @DeleteMapping
    fun deleteAllCategoria(): String{
        this.categoriaService.deleteAll()
        return "Todas as categorias foram deletadas com sucesso"
    }



}