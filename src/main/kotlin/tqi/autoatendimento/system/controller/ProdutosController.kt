package tqi.autoatendimento.system.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tqi.autoatendimento.system.dto.ProdutosAlterarDto
import tqi.autoatendimento.system.dto.ProdutosDto
import tqi.autoatendimento.system.entity.Produtos
import tqi.autoatendimento.system.service.impl.ProdutosService
import java.lang.IllegalArgumentException
import java.util.*

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/produtos")
class ProdutosController(private val produtosService: ProdutosService) {

    //Salva um produto novo
    @PostMapping
    fun saveProduto(@RequestBody @Valid produtosDto: ProdutosDto): ResponseEntity<String>{
        return try {
            val produto: Produtos = this.produtosService.save(produtosDto.toEntity())
            val response: String = "O produto '${produto.nome}' foi adicionado com sucesso!"
            ResponseEntity.status(HttpStatus.OK).body(response)
        }catch(e: IllegalArgumentException){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        }
    }

    //Retorna todos os produtos por nome, categoria ou sem condições
    @GetMapping
    fun getAllProdutos(@RequestParam(required = false) categoriaId: Long?,
                       @RequestParam(required = false, defaultValue = "") nome: String): ResponseEntity<Optional<List<Produtos>>> {
        //Se o parâmetro da categoria existir
        if(categoriaId != null && categoriaId > 0) return ResponseEntity.ok().body(Optional.of(this.produtosService.findAllByCategoria(categoriaId)))

        //Se o parâmetro do nome não estiver vazio
        if(nome.isNotEmpty()) return ResponseEntity.ok().body(Optional.of(this.produtosService.findAllProdutosByName(nome)))

        //Se nenhuma condição for mencionada
        val produtos: List<Produtos> = this.produtosService.findAllProdutos()
        return ResponseEntity.status(HttpStatus.OK).body(Optional.of(produtos))
    }

    //Retorna um produto pelo ID
    @GetMapping("/s")
    fun getProdutosById(@RequestParam("id") @Valid id: Long): ResponseEntity<Optional<Produtos>>{
        return ResponseEntity.status(HttpStatus.OK).body(this.produtosService.findById(id))

    }

    //Altera um produto
    @PutMapping("/alterar")
    fun editProduto(@RequestBody @Valid produtosAlterarDto: ProdutosAlterarDto): ResponseEntity<String> {
        var response: String = produtosService.editProdutos(produtosAlterarDto.toEntity())
        if(response == "Produto não encontrado.") return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        else return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    //Deleta um produto pelo ID
    @DeleteMapping("/{id}")
    fun deleteProdutoById(@PathVariable id: Long): ResponseEntity<String>{
        this.produtosService.delete(id)
        return ResponseEntity.status(HttpStatus.OK).body("Produto de ID:$id excluído com sucesso.")
    }

}