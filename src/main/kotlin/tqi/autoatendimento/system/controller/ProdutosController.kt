package tqi.autoatendimento.system.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tqi.autoatendimento.system.Dto.CategoriaDto
import tqi.autoatendimento.system.Dto.ProdutosAlterarDto
import tqi.autoatendimento.system.Dto.ProdutosDto
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.entity.Produtos
import tqi.autoatendimento.system.service.impl.ProdutosService
import java.lang.IllegalArgumentException
import java.util.*

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/produtos")
class ProdutosController(private val produtosService: ProdutosService) {

    @PostMapping
    fun saveProduto(@RequestBody @Valid produtosDto: ProdutosDto): ResponseEntity<String>{
        try {
            val produto: Produtos = this.produtosService.save(produtosDto.toEntity())
            val response: String = "O produto '${produto.nome}' foi adicionado com sucesso!"
            return ResponseEntity.status(HttpStatus.OK).body(response)
        }catch(e: IllegalArgumentException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        }
    }

    @GetMapping
    fun getAllProdutos(@RequestParam(required = false) categoriaId: Long?, @RequestParam(required = false, defaultValue = "") nome: String): ResponseEntity<Optional<List<Produtos>>> {
        if(categoriaId != null && categoriaId > 0) return ResponseEntity.ok().body(Optional.of(this.produtosService.findAllByCategoria(categoriaId)))
        if(nome.isNotEmpty()) return ResponseEntity.ok().body(Optional.of(this.produtosService.findAllProdutosByName(nome)))
        val produtos: List<Produtos> = this.produtosService.findAllProdutos()
        return ResponseEntity.status(HttpStatus.OK).body(Optional.of(produtos))
    }

    @GetMapping("/s")
    fun getProdutosById(@RequestParam("id") @Valid id: Long): ResponseEntity<Optional<Produtos>>{
        return ResponseEntity.status(HttpStatus.OK).body(this.produtosService.findById(id))

    }

    @PutMapping("/alterar")
    fun editProduto(@RequestBody @Valid produtosAlterarDto: ProdutosAlterarDto): ResponseEntity<String> {
        var response: String = produtosService.editProdutos(produtosAlterarDto.toEntity())
        if(response == "Produto não encontrado.") return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        else return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    @DeleteMapping("/{id}")
    fun deleteProdutoById(@PathVariable id: Long): ResponseEntity<String>{
        this.produtosService.delete(id)
        return ResponseEntity.status(HttpStatus.OK).body("Produto de ID:$id excluído com sucesso.")
    }

}