package tqi.autoatendimento.system.service.impl

import org.springframework.stereotype.Service
import tqi.autoatendimento.system.entity.Carrinho
import tqi.autoatendimento.system.repository.CarrinhoRepository
import tqi.autoatendimento.system.service.ICarrinhoService

@Service
class CarrinhoService(private val carrinhoRepository: CarrinhoRepository): ICarrinhoService {
    override fun save(carrinho: Carrinho): Carrinho {
        TODO("Not yet implemented")
    }

    override fun findCarrinho(carrinho: Carrinho): Carrinho {
        TODO("Not yet implemented")
    }

    override fun delete(carrinho: Carrinho): Carrinho {
        TODO("Not yet implemented")
    }

}