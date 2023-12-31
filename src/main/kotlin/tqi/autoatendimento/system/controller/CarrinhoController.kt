package tqi.autoatendimento.system.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tqi.autoatendimento.system.dto.CarrinhoDto
import tqi.autoatendimento.system.entity.Carrinho
import tqi.autoatendimento.system.service.impl.CarrinhoService

@RestController
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RequestMapping("/api/carrinho")
class CarrinhoController(private val carrinhoService: CarrinhoService) {

    //Salva um produto no carrinho
    @PostMapping()
    fun saveCarrinho(@RequestBody @Valid carrinhoDto: CarrinhoDto): ResponseEntity<String>{
        val response: String = this.carrinhoService.saveCarrinho(carrinhoDto.toEntity())

        return if(response == "O carrinho foi atualizado com sucesso.") ResponseEntity.status(HttpStatus.OK).body(response)
        else ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    //Retorna a lista de produtos no carrinho
    @GetMapping
    fun getCarrinho(): ResponseEntity<List<Carrinho>>{
        val carrinho: List<Carrinho> = this.carrinhoService.findCarrinho()
        return ResponseEntity.status(HttpStatus.OK).body(carrinho)
    }

    //Altera os produtos do carrinho
    @PatchMapping("/update")
    fun updateCarrinho(@RequestBody carrinhoDto: CarrinhoDto): ResponseEntity<String>{
        this.carrinhoService.updateCarrinho(carrinhoDto.toEntity())
        val response = "O carrinho foi alterado com sucesso."
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    //Deleta um produto pelo ID do carrinho
    @DeleteMapping("/{id}")
    fun deleteCarrinhoById(@PathVariable id: Long){
        this.carrinhoService.deleteById(id)
    }

    //Deleta o carrinho inteiro
    @DeleteMapping
    fun deleteCarrinho(){
        this.carrinhoService.deleteAll()
    }

}