package tqi.autoatendimento.system.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import tqi.autoatendimento.system.repository.ProdutosRepository
import tqi.autoatendimento.system.service.impl.ProdutosService
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertEquals
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.entity.Produtos
import tqi.autoatendimento.system.enum.UnidadeMedida
import tqi.autoatendimento.system.repository.CategoriaRepository
import java.math.BigDecimal
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class ProdutosServiceTest {
    @MockK lateinit var produtosRepository: ProdutosRepository
    @InjectMockKs lateinit var produtosService: ProdutosService

    @MockK lateinit var categoriaRepository: CategoriaRepository

    //Método para testar se o produto é salvo quando a categoria existe
    @Test
    fun `should save produto when categoria exists`() {
        // given
        val fakeCategoria: Categoria = buildCategoria()
        val fakeProduto: Produtos = buildProdutos()

        every { categoriaRepository.existsById(fakeCategoria.id!!) } returns true
        every { produtosRepository.save(fakeProduto) } returns fakeProduto

        // when
        val actual: Produtos = produtosService.save(fakeProduto)

        // then
        assertThat(actual).isEqualTo(fakeProduto)
        verify(exactly = 1) { categoriaRepository.existsById(fakeCategoria.id!!) }
        verify(exactly = 1) { produtosRepository.save(fakeProduto) }
    }

    //Método para jogar uma exceção se a categoria não existir ao salvar o produto
    @Test
    fun `should throw IllegalArgumentException when categoria does not exist`() {
        // given
        val fakeCategoria: Categoria = buildCategoria()
        val fakeProduto: Produtos = buildProdutos()

        every { categoriaRepository.existsById(fakeCategoria.id!!) } returns false

        // when/then
        assertThatThrownBy { produtosService.save(fakeProduto) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Categoria não existe.")
        verify(exactly = 1) { categoriaRepository.existsById(fakeCategoria.id!!) }
    }

    //Método para encontrar todos os produtos pela categoria
    @Test
    fun `should find all produtos by categoria`() {
        //given
        val fakeCategoria: Categoria = buildCategoria()
        val fakeProdutos1: Produtos = buildProdutos()
        val fakeProdutos2: Produtos = buildProdutos(id = 2L, nome = "X-burguer")
        val fakeProdutosList: List<Produtos> = listOf(fakeProdutos1, fakeProdutos2)
        every { produtosRepository.findAllByCategoria(fakeCategoria.id!!) } returns fakeProdutosList

        //when
        val actual: List<Produtos> = produtosService.findAllByCategoria(fakeCategoria.id!!)

        //then
        assertThat(actual).hasSize(2)
        assertThat(actual).containsExactlyInAnyOrder(fakeProdutos1, fakeProdutos2)
        verify(exactly = 1) { produtosRepository.findAllByCategoria(fakeCategoria.id!!) }
    }

    //Método para encontrar um produto pelo id
    @Test
    fun `should find produtos by id`() {
        //given
        val fakeCategoria: Categoria = buildCategoria(nome = "fakeCategoria")
        val fakeProdutos: Produtos = buildProdutos(nome = "fakeProdutos", categoria = fakeCategoria)
        every { produtosRepository.findById(fakeProdutos.id!!) } returns Optional.of(fakeProdutos)

        //when
        val actual: Optional<Produtos> = produtosService.findById(fakeProdutos.id!!)

        //then
        assertThat(actual).isNotEmpty
        assertThat(actual.get()).isEqualTo(fakeProdutos)
        verify(exactly = 1) { produtosRepository.findById(fakeProdutos.id!!) }
    }

    //Método para não encontrar um produto se um ID for inválido
    @Test
    fun `should not find produtos by invalid id`() {
        //given
        val fakeId = 50L
        every { produtosRepository.findById(fakeId) } returns Optional.empty()

        //when
        val actual: Optional<Produtos> = produtosService.findById(fakeId)

        //then
        assertThat(actual).isEmpty
        verify(exactly = 1) { produtosRepository.findById(fakeId) }
    }

    //Método pra encontrar todos os produtos, sem condição
    @Test
    fun `should return all produtos`() {
        //given
        val fakeCategoria: Categoria = buildCategoria()
        val fakeProdutosList: List<Produtos> = listOf(
            buildProdutos(),
            buildProdutos(id = 2L, nome = "X-burguer", categoria = fakeCategoria),
            buildProdutos(id = 3L, nome = "X-tudo", categoria = fakeCategoria)
        )
        every { produtosRepository.findAll() } returns fakeProdutosList

        //when
        val result: List<Produtos> = produtosService.findAllProdutos()

        //then
        assertThat(result).isEqualTo(fakeProdutosList)
        verify(exactly = 1) { produtosRepository.findAll() }
    }

    //Método para mostrar uma lista vazia se nenhum produto existir
    @Test
    fun `should find empty list when no produtos exist`() {
        //given
        val fakeProdutosList: List<Produtos> = emptyList()
        every { produtosRepository.findAll() } returns fakeProdutosList

        //when
        val actual: List<Produtos> = produtosService.findAllProdutos()

        //then
        assertThat(actual).isEmpty()
        verify(exactly = 1) { produtosRepository.findAll() }
    }

    //Método para encontrar produtos pelo nome
    @Test
    fun `should find produtos by name`() {
        //given
        val nomeProdutoBuscado = "sal"
        val fakeProdutos1: Produtos = buildProdutos()
        every { produtosRepository.findAllByNome(nomeProdutoBuscado) } returns listOf(fakeProdutos1)

        //when
        val actual: List<Produtos> = produtosService.findAllProdutosByName(nomeProdutoBuscado)

        //then
        assertThat(actual).isNotEmpty
        assertThat(actual).hasSize(1)
        assertThat(actual).containsExactly(fakeProdutos1)
        verify(exactly = 1) { produtosRepository.findAllByNome(nomeProdutoBuscado) }
    }

    //Método para alterar produtos
    @Test
    fun `should edit produtos`() {
        //given
        val fakeProduto: Produtos = buildProdutos(nome = "X-tudo", quantidade = 17)
        every {
            produtosRepository.editProdutos(
                fakeProduto.id!!,
                fakeProduto.nome,
                fakeProduto.unidadeDeMedida.name,
                fakeProduto.precoUnitario,
                fakeProduto.categoria.id!!,
                fakeProduto.quantidade
            )
        } returns 1

        //when
        val actual: String = produtosService.editProdutos(fakeProduto)

        //then
        val message = "O produto de ID: ${fakeProduto.id} foi alterado com sucesso!"
        assertThat(actual).isEqualTo(message)
    }

    //Método para retornar que o produto não foi encontrado para ser alterado
    @Test
    fun `should return 'Produto não encontrado' when produto is not found`() {
        //given
        val fakeProduto: Produtos = buildProdutos()
        every { produtosRepository.editProdutos(any(), any(), any(), any(), any(), any()) } returns 0

        //when
        val actual: String = produtosService.editProdutos(fakeProduto)

        //then
        assertEquals("Produto não encontrado.", actual)
        verify(exactly = 1) { produtosRepository.editProdutos(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `should delete produtos by Id`() {
        //given
        val fakeProdutos: Produtos = buildProdutos()
        every { produtosRepository.deleteById(fakeProdutos.id!!) } just runs

        //when
        produtosService.delete(fakeProdutos.id!!)

        //then
        verify(exactly = 1) { produtosRepository.deleteById(fakeProdutos.id!!) }
    }

    //Método privado para criar produtos que seram usados para o teste
    private fun buildProdutos(
        id: Long = 1L,
        nome: String = "X-salada",
        precoUnitario: BigDecimal = BigDecimal(6.50),
        quantidade: Int = 3,
        unidadeMedida: UnidadeMedida = UnidadeMedida.UNIDADE,
        categoria: Categoria = buildCategoria()
    ) = Produtos(
        id = id,
        nome = nome,
        precoUnitario = precoUnitario,
        quantidade = quantidade,
        unidadeDeMedida = unidadeMedida,
        categoria = categoria
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