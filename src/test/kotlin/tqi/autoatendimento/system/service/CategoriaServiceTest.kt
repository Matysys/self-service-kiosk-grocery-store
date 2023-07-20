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
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.repository.CategoriaRepository
import tqi.autoatendimento.system.service.impl.CategoriaService
import org.assertj.core.api.Assertions.*
import java.lang.RuntimeException
import java.util.Optional
import java.util.Random

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CategoriaServiceTest {
    @MockK lateinit var categoriaRepository: CategoriaRepository
    @InjectMockKs lateinit var categoriaService: CategoriaService

    //Método para testar se a categoria é salva com sucesso
    @Test
    fun `should save categoria`(){
        //given
        val fakeCategoria: Categoria = buildCategoria()
        every { categoriaRepository.save(any()) } returns fakeCategoria

        //when
        val actual: Categoria = categoriaService.save(fakeCategoria)

        //then
        assertThat(actual).isNotNull
        assertThat(actual).isSameAs(fakeCategoria)

        verify(exactly = 1){ categoriaRepository.save(fakeCategoria) }
    }

    @Test
    fun `should return all categorias`() {
        // given
        val fakeCategoria1 = Categoria()
        val fakeCategoria2 = Categoria(id = 2, nome = "Japonesa")
        val fakeCategorias: List<Categoria> = listOf(fakeCategoria1, fakeCategoria2)

        every { categoriaRepository.findAll() } returns fakeCategorias

        // when
        val actual: List<Categoria> = categoriaService.findAllCategoria()

        // then
        assertThat(actual).isEqualTo(fakeCategorias)
    }

    //Método para retornar a categoria pelo Id
    @Test
    fun `should return categoria by id`(){
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCategoria: Categoria = buildCategoria(id = fakeId)
        every { categoriaRepository.findById(fakeId) } returns Optional.of(fakeCategoria)

        //when
        val actual: Categoria = categoriaService.findById(fakeId)

        //then
        assertThat(actual).isExactlyInstanceOf(Categoria::class.java)
        assertThat(actual).isNotNull
        verify(exactly = 1){ categoriaRepository.findById(fakeId) }
    }

    @Test
    fun `should not find categoria by invalid id and throw BusinessException`() {
        //given
        val fakeId: Long = Random().nextLong()
        every { categoriaRepository.findById(fakeId) } returns Optional.empty()
        //when
        //then
        assertThatExceptionOfType(RuntimeException::class.java)
            .isThrownBy { categoriaService.findById(fakeId) }
            .withMessage("Id $fakeId não encontrado.")
        verify(exactly = 1) { categoriaRepository.findById(fakeId) }
    }

    //Método pra deletar a categoria pelo id
    @Test
    fun `should delete categoria by id`() {
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCategoria: Categoria = buildCategoria(id = fakeId)
        every { categoriaRepository.findById(fakeId) } returns Optional.of(fakeCategoria)
        every { categoriaRepository.deleteById(fakeId) } just runs
        //when
        categoriaService.deleteById(fakeId)
        //then
        verify(exactly = 1) { categoriaRepository.deleteById(fakeId) }
    }

    @Test
    fun `should delete all categorias`() {
        // given
        val fakeCategoria: Categoria = buildCategoria()
        val fakeCategoria2: Categoria = buildCategoria(id = 2, nome = "Japonesa")
        val fakeCategorias: List<Categoria> = listOf(fakeCategoria, fakeCategoria2)
        every { categoriaRepository.findAll() } returns fakeCategorias
        every { categoriaRepository.deleteAll() } just runs

        // when
        categoriaService.deleteAll()

        // then
        verify(exactly = 1) { categoriaRepository.deleteAll() }
    }

    private fun buildCategoria(
        id: Long = 1L,
        nome: String = "Lanches"
    ) = Categoria(
        id = id,
        nome = nome
    )

}