package tqi.autoatendimento.system.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tqi.autoatendimento.system.dto.CategoriaDto
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.service.impl.CategoriaService
import java.util.*

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/categoria")
class CategoriaController(private val categoriaService: CategoriaService) {

    @PostMapping
    fun saveCategoria(@RequestBody @Valid categoriaDto: CategoriaDto): ResponseEntity<String>{
        val categoria: Categoria = this.categoriaService.save(categoriaDto.toEntity())
        val response: String = "A categoria '${categoria.nome}' foi adicionada com sucesso!"
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @GetMapping("/{id}")
    fun getCategoriaById(@PathVariable id: Long): ResponseEntity<Optional<Categoria>> {
        val categoria: Optional<Categoria> = Optional.of(this.categoriaService.findById(id))
        return ResponseEntity.status(HttpStatus.OK).body(categoria)
    }

    @GetMapping
    fun getAllCategoria(): ResponseEntity<Optional<List<Categoria>>> {
        val categorias: List<Categoria> = this.categoriaService.findAllCategoria()
        return ResponseEntity.status(HttpStatus.OK).body(Optional.of(categorias))
    }

    @DeleteMapping("/{id}")
    fun deleteCategoriaById(@PathVariable id: Long): ResponseEntity<String>{
        this.categoriaService.deleteById(id)
        val response = "Categoria de número $id deletada com sucesso"
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @DeleteMapping
    fun deleteAllCategoria(): ResponseEntity<String>{
        this.categoriaService.deleteAll()
        val response = "Todas as categorias foram deletadas com sucesso"
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }



}