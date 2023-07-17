package tqi.autoatendimento.system.controller

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import tqi.autoatendimento.system.Dto.CategoriaDto
import tqi.autoatendimento.system.Dto.ProdutosAlterarDto
import tqi.autoatendimento.system.Dto.ProdutosDto
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.entity.Produtos
import tqi.autoatendimento.system.service.impl.ProdutosService
import java.util.*

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/produtos")
class ProdutosController(private val produtosService: ProdutosService) {

    @PostMapping
    fun saveProduto(@RequestBody @Valid produtosDto: ProdutosDto): String{
        val produto = this.produtosService.save(produtosDto.toEntity())
        return "O produto '${produto.nome}' foi adicionado com sucesso!"
    }

    @GetMapping("/{categoria}")
    fun getProdutosByCategoria(@PathVariable @Valid categoria: String): Optional<List<Produtos>> {
        val categoria: List<Produtos> = this.produtosService.findAllByCategoria(categoria)
        return Optional.of(categoria)
    }

    @GetMapping
    fun getAllProdutos(): Optional<List<Produtos>> {
        val produtos: List<Produtos> = this.produtosService.findAllProdutos()
        return Optional.of(produtos)
    }

    @GetMapping("/busca/{nome}")
    fun getProdutosByNome(@PathVariable @Valid nome: String): Optional<List<Produtos>> {
        val produtos: List<Produtos> = this.produtosService.findAllProdutosByName(nome)
        return Optional.of(produtos)
    }

    @PutMapping("/alterar")
    fun editProduto(@RequestBody @Valid produtosAlterarDto: ProdutosAlterarDto): String {
        return produtosService.editProdutos(produtosAlterarDto.toEntity())
    }

}