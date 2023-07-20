package tqi.autoatendimento.system.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import tqi.autoatendimento.system.repository.ProdutosRepository
import tqi.autoatendimento.system.service.impl.ProdutosService
import org.assertj.core.api.Assertions.*
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.entity.Produtos
import tqi.autoatendimento.system.enum.UnidadeMedida
import tqi.autoatendimento.system.repository.CategoriaRepository
import java.math.BigDecimal

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class ProdutosServiceTest {
    @MockK lateinit var produtosRepository: ProdutosRepository
    @InjectMockKs lateinit var produtosService: ProdutosService

    @MockK lateinit var categoriaRepository: CategoriaRepository

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

    private fun buildCategoria(
        id: Long = 1L,
        nome: String = "Lanches"
    ) = Categoria(
        id = id,
        nome = nome
    )


}