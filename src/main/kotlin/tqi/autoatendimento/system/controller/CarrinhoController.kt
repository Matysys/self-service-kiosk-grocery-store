package tqi.autoatendimento.system.controller

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import tqi.autoatendimento.system.Dto.CarrinhoDto
import tqi.autoatendimento.system.entity.Carrinho
import tqi.autoatendimento.system.service.impl.CarrinhoService

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/carrinho")
class CarrinhoController(private val carrinhoService: CarrinhoService) {

    @PostMapping()
    fun saveCarrinho(@RequestBody @Valid carrinhoDto: CarrinhoDto): String{
        this.carrinhoService.saveCarrinho(carrinhoDto.toEntity())
        return "O carrinho foi atualizado com sucesso."
    }

    @GetMapping
    fun getCarrinho(): List<Carrinho>{
        val carrinho: List<Carrinho> = this.carrinhoService.findCarrinho()
        return carrinho
    }

}