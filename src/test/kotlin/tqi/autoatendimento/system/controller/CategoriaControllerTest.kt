package tqi.autoatendimento.system.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import tqi.autoatendimento.system.dto.CategoriaDto
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.repository.CategoriaRepository

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CategoriaControllerTest {

    //Anotação para injetar a dependências
    @Autowired private lateinit var categoriaRepository: CategoriaRepository
    @Autowired private lateinit var mockMvc: MockMvc
    @Autowired private lateinit var objectMapper: ObjectMapper

    //Endpoint principal da categoria
    companion object{
        const val URL: String = "/api/categoria"
    }

    //Deleta tudo antes de cada teste
    @BeforeEach fun setup(){
        categoriaRepository.deleteAll()
    }

    //Deleta tudo após cada teste
    @AfterEach fun tearDown(){
        categoriaRepository.deleteAll()
    }

    //POST, salva uma nova categoria
    @Test
    fun `should save a new categoria`() {
        // given
        val categoriaDto: CategoriaDto = buildCategoriaDto()
        val valueAsString: String = objectMapper.writeValueAsString(categoriaDto)

        // when
        mockMvc.perform(MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString))
            .andExpect(MockMvcResultMatchers.status().isOk)

        // then
        val categoriasSalvas: MutableList<Categoria> = categoriaRepository.findAll()
        assertThat(categoriasSalvas).hasSize(1)

        val categoriaSalva: Categoria = categoriasSalvas[0]
        assertThat(categoriaSalva.nome).isEqualTo(categoriaDto.nome)
    }

    //GET, retorna a categoria pelo id
    @Test
    fun `should get categoria by id`() {
        //given
        val categoriaSalva: Categoria = categoriaRepository.save(Categoria(id = 1, nome = "Teste"))

        //when
        val actual = mockMvc.perform(MockMvcRequestBuilders.get("$URL/${categoriaSalva.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        //then
        val responseJson: String = actual.response.contentAsString
        val categoriaResponse: Categoria = objectMapper.readValue(responseJson, Categoria::class.java)
        assertThat(categoriaResponse).isNotNull()
        assertThat(categoriaResponse.id).isEqualTo(categoriaSalva.id)
        assertThat(categoriaResponse.nome).isEqualTo(categoriaSalva.nome)
    }

    //GET, retorna todas as categorias
    @Test
    fun `should get all categorias`() {
        // given
        val categoria1: Categoria = categoriaRepository.save(Categoria(id = 1, nome = "Teste1"))
        val categoria2: Categoria = categoriaRepository.save(Categoria(id = 2, nome = "Teste2"))

        // when
        val actual = mockMvc.perform(MockMvcRequestBuilders.get(URL))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        // then
        val responseJson: String = actual.response.contentAsString
        val categoriaResponseList = objectMapper.readValue<List<Categoria>>(responseJson, object : TypeReference<List<Categoria>>() {})
        assertThat(categoriaResponseList).isNotEmpty()
        assertThat(categoriaResponseList).hasSize(2)
        assertThat(categoriaResponseList).contains(categoria1, categoria2)
    }

    //DELETE, delete uma categoria pelo id
    @Test
    fun `should delete categoria by id`() {
        // given
        val categoria = categoriaRepository.save(Categoria(id = 2, nome = "Teste"))

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/${categoria.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)

        // then
        assertThat(categoriaRepository.existsById(categoria.id!!)).isFalse()
    }

    //DELETE, deleta todas as categorias
    @Test
    fun `should delete all categorias`() {
        // given
        categoriaRepository.save(Categoria(id = 1, nome = "Teste"))
        categoriaRepository.save(Categoria(id = 2, nome = "Teste2"))

        // when
        mockMvc.perform(MockMvcRequestBuilders.delete(URL))
            .andExpect(MockMvcResultMatchers.status().isOk)

        // then
        assertThat(categoriaRepository.count()).isEqualTo(0)
    }

    //Dto da categoria
    private fun buildCategoriaDto(
        nome: String = "Lanches"
    ) = CategoriaDto(
        nome = nome
    )

}