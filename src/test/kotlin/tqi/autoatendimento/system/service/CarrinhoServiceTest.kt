package tqi.autoatendimento.system.service

import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import tqi.autoatendimento.system.repository.CarrinhoRepository
import tqi.autoatendimento.system.service.impl.CarrinhoService
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertEquals
import tqi.autoatendimento.system.entity.Carrinho
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.entity.Produtos
import tqi.autoatendimento.system.enum.UnidadeMedida
import tqi.autoatendimento.system.repository.ProdutosRepository
import java.math.BigDecimal

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CarrinhoServiceTest {
    @MockK lateinit var carrinhoRepository: CarrinhoRepository
    @MockK lateinit var produtosRepository: ProdutosRepository
    @InjectMockKs lateinit var carrinhoService: CarrinhoService

    //Método para salvar um produto no carrinho
    @Test
    fun `should return 'O carrinho foi atualizado com sucesso' when produto is added to carrinho`() {
        //given
        val fakeCarrinho: Carrinho = buildCarrinho()
        every { produtosRepository.calcularPreco(fakeCarrinho.idProduto, fakeCarrinho.nomeProduto, fakeCarrinho.quantidadeProduto) } returns BigDecimal(19.50)
        every { produtosRepository.verificarQntProdutos(any()) } returns 20
        every { carrinhoRepository.existsCarrinho(any()) } returns null
        every { carrinhoRepository.save(any()) } returns fakeCarrinho

        //when
        val actual: String = carrinhoService.saveCarrinho(fakeCarrinho)

        //then
        assertEquals("O carrinho foi atualizado com sucesso.", actual)
        verify(exactly = 1) { produtosRepository.calcularPreco(any(), any(), any()) }
        verify(exactly = 1) { produtosRepository.verificarQntProdutos(any()) }
        verify(exactly = 1) { carrinhoRepository.existsCarrinho(any()) }
        verify(exactly = 1) { carrinhoRepository.save(any()) }
    }

    //Método para retornar que o produto já existe no carrinho
    @Test
    fun `should return 'Produto já está no carrinho' when produto is already in carrinho`() {
        // given
        val fakeCarrinho: Carrinho = buildCarrinho()
        every { produtosRepository.calcularPreco(fakeCarrinho.idProduto, fakeCarrinho.nomeProduto, fakeCarrinho.quantidadeProduto) } returns BigDecimal(19.50)
        every { produtosRepository.verificarQntProdutos(any()) } returns 20
        every { carrinhoRepository.existsCarrinho(any()) } returns fakeCarrinho.idProduto

        // when
        val actual: String = carrinhoService.saveCarrinho(fakeCarrinho)

        // then
        assertEquals("Produto já está no carrinho.", actual)
        verify(exactly = 1) { produtosRepository.calcularPreco(any(), any(), any()) }
        verify(exactly = 1) { produtosRepository.verificarQntProdutos(any()) }
        verify(exactly = 1) { carrinhoRepository.existsCarrinho(any()) }
    }

    //Método para retornar que não há estoque
    @Test
    fun `should return 'Não há estoque o suficiente para a quantidade solicitada' when produto has insufficient stock`() {
        // given
        val fakeCarrinho: Carrinho = buildCarrinho()
        every { produtosRepository.calcularPreco(fakeCarrinho.idProduto, fakeCarrinho.nomeProduto, fakeCarrinho.quantidadeProduto) } returns BigDecimal(19.50)
        every { produtosRepository.verificarQntProdutos(any()) } returns 2

        // when
        val actual: String = carrinhoService.saveCarrinho(fakeCarrinho)

        // then
        assertEquals("Não há estoque o suficiente para a quantidade solicitada.", actual)
        verify(exactly = 1) { produtosRepository.calcularPreco(any(), any(), any()) }
        verify(exactly = 1) { produtosRepository.verificarQntProdutos(any()) }
    }

    //Método para atualizar um produto do carrinho
    @Test
    fun `should update carrinho correctly`() {
        // given
        val fakeCarrinho: Carrinho = buildCarrinho()
        val precoCalculado: BigDecimal = BigDecimal(19.50)
        every { produtosRepository.calcularPreco(fakeCarrinho.idProduto, fakeCarrinho.nomeProduto, fakeCarrinho.quantidadeProduto) } returns precoCalculado
        every { carrinhoRepository.update(fakeCarrinho.nomeProduto, fakeCarrinho.quantidadeProduto, precoCalculado) } just runs

        // when
        carrinhoService.updateCarrinho(fakeCarrinho)

        // then
        verify(exactly = 1) { produtosRepository.calcularPreco(any(), any(), any()) }
        verify(exactly = 1) { carrinhoRepository.update(fakeCarrinho.nomeProduto, fakeCarrinho.quantidadeProduto, precoCalculado) }
    }

    //Método pra retornar todos os produtos do carrinho
    @Test
    fun `should return all produtos no carrinho`() {
        // given
        val fakeCarrinhos: List<Carrinho> = listOf(
            buildCarrinho(),
            buildCarrinho(id = 2L, nomeProduto = "X-burger", quantidadeProduto = 4)
        )
        every { carrinhoRepository.findAllProdutos() } returns fakeCarrinhos

        // when
        val carrinhos: List<Carrinho> = carrinhoService.findCarrinho()

        //then
        assertEquals(fakeCarrinhos, carrinhos)
        verify(exactly = 1) { carrinhoRepository.findAllProdutos() }
    }

    //Método para deletar um produto do carrinho pelo ID
    @Test
    fun `should delete carrinho by id`() {
        //given
        val fakeCarrinho: Carrinho = buildCarrinho()
        every { carrinhoRepository.deleteById(fakeCarrinho.id!!) } just runs

        //when
        carrinhoService.deleteById(fakeCarrinho.id!!)

        //then
        verify(exactly = 1) { carrinhoRepository.deleteById(fakeCarrinho.id!!) }
    }

    //Método para deletar todo o carrinho
    @Test
    fun `should delete all carrinhos`() {
        //given
        buildCarrinho()
        every { carrinhoRepository.deleteAll() } just runs

        // when
        carrinhoService.deleteAll()

        // then
        verify(exactly = 1) { carrinhoRepository.deleteAll() }
    }

    @Test
    fun `should truncate all carrinhos`() {
        // given
        buildCarrinho()
        every { carrinhoRepository.truncateAll() } just runs

        // when
        carrinhoService.truncateAll()

        // then
        verify(exactly = 1) { carrinhoRepository.truncateAll() }
        confirmVerified(carrinhoRepository)
    }

    //Método privado para criar um carrinho
    private fun buildCarrinho(
        id: Long = 1L,
        idProduto: Long = 1L,
        nomeProduto: String = "X-salada",
        quantidadeProduto: Int = 3,
        precoProduto: BigDecimal = BigDecimal(5.50)
    ) = Carrinho(
        id = id,
        idProduto = idProduto,
        nomeProduto = nomeProduto,
        quantidadeProduto = quantidadeProduto,
        precoProduto = precoProduto
    )

    //Método privado para criar categorias que seram usados para o teste
    private fun buildCategoria(
        id: Long = 1L,
        nome: String = "Lanches"
    ) = Categoria(
        id = id,
        nome = nome
    )


}


