package tqi.autoatendimento.system.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.entity.Produtos
import tqi.autoatendimento.system.enum.UnidadeMedida
import java.math.BigDecimal

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProdutosRepositoryTest {
    @Autowired lateinit var produtosRepository: ProdutosRepository
    @Autowired lateinit var testEntityManager: TestEntityManager

    private lateinit var produtos: Produtos
    private lateinit var categoria: Categoria
    @Autowired
    private lateinit var categoriaRepository: CategoriaRepository

    @BeforeEach fun setup(){
        categoria = testEntityManager.persist(buildCategoria())
        produtos = testEntityManager.persist(buildProdutos(categoria = categoria))
        testEntityManager.persist(buildProdutos(nome = "X-bacon", categoria = categoria))
        testEntityManager.persist(buildProdutos(nome = "Hamburguer", categoria = categoria))
    }

    @AfterEach fun teardown(){
        produtosRepository.deleteAll()
        categoriaRepository.deleteAll()
    }

    //SELECT, encontra todos os produtos pela categoria
    @Test
    fun `should find all produtos by categoria`() {
        //when
        val produtosByCategoria: List<Produtos> = produtosRepository.findAllByCategoria(categoria.id!!)

        //then
        assertThat(produtosByCategoria).isNotEmpty
        assertThat(produtosByCategoria).hasSize(3)
        assertThat(produtosByCategoria[0]).isEqualTo(produtos)
        assertThat(produtosByCategoria[0].categoria).isEqualTo(categoria)
    }

    //SELECT %LIKE%, encontra todos os produtos pelo nome, não importando a ordem
    @Test
    fun `should find all produtos by nome containing the given value`() {
        //given
        val nome = "X-"

        //when
        val produtosByNome: List<Produtos> = produtosRepository.findAllByNome(nome)

        //then
        assertThat(produtosByNome).isNotEmpty
        assertThat(produtosByNome).hasSize(2)
        assertThat(produtosByNome.map { it.nome }).containsExactlyInAnyOrder("X-salada", "X-bacon")
    }

    //SELECT, calcula o preço total de acordo com o preço do produto e quantidade
    @Test
    fun `should calculate the total price for a given product and quantity`() {
        //when
        val precoTotal: BigDecimal = produtosRepository.calcularPreco(produtos.id!!, produtos.nome, produtos.quantidade)

        //then
        val expectedPrecoTotal = BigDecimal("19.50") // 6,50 * 3 = 19.50
        assertThat(precoTotal).isEqualTo(expectedPrecoTotal)
    }

    //UPDATE, altera um produto e retorna o número de linhas alteradas
    @Test
    fun `should edit produtos and return the number of updated rows`() {
        //given
        val novoNome = "X-tudo"
        val novaUnidadeMedida = "UNIDADE"
        val novoPrecoUnitario = BigDecimal("7.50")
        val novaCategoria: Categoria = testEntityManager.persist(buildCategoria(nome = "Lanches2"))
        val novaQuantidade = 5

        //when
        val linhas: Int = produtosRepository.editProdutos(produtos.id!!, novoNome, novaUnidadeMedida, novoPrecoUnitario, novaCategoria.id!!, novaQuantidade)

        //then
        assertThat(linhas).isEqualTo(1)
        testEntityManager.flush()
        testEntityManager.clear()
        val produtoAtualizado: Produtos = testEntityManager.find(Produtos::class.java, produtos.id)
        assertThat(produtoAtualizado).isNotNull
        assertThat(produtoAtualizado.nome).isEqualTo(novoNome)
        assertThat(produtoAtualizado.unidadeDeMedida.toString()).isEqualTo(novaUnidadeMedida)
        assertThat(produtoAtualizado.precoUnitario).isEqualTo(novoPrecoUnitario)
        assertThat(produtoAtualizado.categoria.id).isEqualTo(novaCategoria.id)
        assertThat(produtoAtualizado.quantidade).isEqualTo(novaQuantidade)
    }

    //SELECT, retorna a quantidade de produtos pelo id
    @Test
    fun `should return the quantity of produtos with the given id`() {
        //given
        val produtoId: Long = produtos.id!!
        val quantidadeEsperada = 3

        //when
        val quantidadeAtual: Int = produtosRepository.verificarQntProdutos(produtoId)

        //then
        assertThat(quantidadeAtual).isEqualTo(quantidadeEsperada)
    }

    //UPDATE, altera a quantidade de produtos pelo id
    @Test
    fun `should update the quantidade of produtos with the given id`() {
        //given
        val quantidadeInicial = produtos.quantidade
        val produtoId: Long = 1
        val quantidadeRemovida = 2

        //when
        produtosRepository.removerEstoqueProduto(produtoId, quantidadeRemovida)
        testEntityManager.flush()
        testEntityManager.clear()

        //then
        val produtoAtualizado: Produtos = testEntityManager.find(Produtos::class.java, produtoId)
        assertThat(produtoAtualizado).isNotNull
        assertThat(produtoAtualizado.quantidade).isEqualTo(quantidadeInicial - quantidadeRemovida)
    }

    private fun buildCategoria(
        nome: String = "Lanches"
    ) = Categoria(
        nome = nome
    )

    private fun buildProdutos(
        nome: String = "X-salada",
        precoUnitario: BigDecimal = BigDecimal(6.50),
        quantidade: Int = 3,
        unidadeMedida: UnidadeMedida = UnidadeMedida.UNIDADE,
        categoria: Categoria = buildCategoria() // Remove a criação de nova categoria aqui
    ): Produtos = Produtos(
        nome = nome,
        precoUnitario = precoUnitario,
        quantidade = quantidade,
        unidadeDeMedida = unidadeMedida,
        categoria = categoria
    )
}