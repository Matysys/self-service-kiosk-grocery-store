package tqi.autoatendimento.system.service.impl

import org.springframework.stereotype.Service
import tqi.autoatendimento.system.entity.Carrinho
import tqi.autoatendimento.system.repository.CarrinhoRepository
import tqi.autoatendimento.system.repository.ProdutosRepository
import tqi.autoatendimento.system.service.ICarrinhoService

//Regras de negócio para o carrinho
@Service
class CarrinhoService(private val carrinhoRepository: CarrinhoRepository
, private val produtosRepository: ProdutosRepository): ICarrinhoService {

    //Salva um novo produto no carrinho de acordo com as regras
    override fun saveCarrinho(carrinho: Carrinho): String {
        //Calcula o preço total dos produtos
        carrinho.precoProduto = this.produtosRepository.calcularPreco(carrinho.idProduto, carrinho.nomeProduto, carrinho.quantidadeProduto)

        //Verifica a quantidade atual do produto em questão
        val quantidadeAtual: Int = this.produtosRepository.verificarQntProdutos(carrinho.idProduto)

        //Verifica se a quantidade de produtos do carrinho é menor ou igual ao do estoque do produto
        return if (carrinho.quantidadeProduto <= quantidadeAtual) {
            //Verifica se o produto já existe no carrinho antes de adicionar novamente
            val exist: Long? = this.carrinhoRepository.existsCarrinho(carrinho.idProduto)
            //Se existir, retorna a mensagem
            if (exist != null) {
                "Produto já está no carrinho."
            }else{
                //Salva o produto no carrinho
                this.carrinhoRepository.save(carrinho)
                "O carrinho foi atualizado com sucesso."
            }
        }else{
            //Retorna essa mensagem se o estoque for insuficiente
            "Não há estoque o suficiente para a quantidade solicitada."
        }

    }

    //Altera um produto do carrinho
    override fun updateCarrinho(carrinho: Carrinho) {
        carrinho.precoProduto = this.produtosRepository.calcularPreco(carrinho.idProduto, carrinho.nomeProduto, carrinho.quantidadeProduto)
        this.carrinhoRepository.update(carrinho.nomeProduto, carrinho.quantidadeProduto, carrinho.precoProduto)
    }

    //Retorna a lista dos produtos que estão no carrinho
    override fun findCarrinho(): List<Carrinho> {
        return this.carrinhoRepository.findAllProdutos()
    }

    //Delete um produto do carrinho pelo ID do carrinho
    override fun deleteById(id: Long) {
        this.carrinhoRepository.deleteById(id)
    }

    //Deleta o carrinho inteiro
    override fun deleteAll(){
        this.carrinhoRepository.deleteAll()
    }

    //TRUNCATE na tabela, reseta os IDs e apaga a tabela
    override fun truncateAll(){
        this.carrinhoRepository.truncateAll()
    }
}