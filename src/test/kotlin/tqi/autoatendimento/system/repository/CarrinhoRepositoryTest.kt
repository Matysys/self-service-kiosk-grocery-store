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
import tqi.autoatendimento.system.entity.Carrinho
import tqi.autoatendimento.system.entity.Categoria
import tqi.autoatendimento.system.entity.Produtos
import tqi.autoatendimento.system.enum.UnidadeMedida
import java.math.BigDecimal

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CarrinhoRepositoryTest {
    @Autowired lateinit var carrinhoRepository: CarrinhoRepository
    @Autowired lateinit var testEntityManager: TestEntityManager

    private lateinit var produtos: Produtos
    private lateinit var produtos2: Produtos
    private lateinit var categoria: Categoria
    private lateinit var carrinho: Carrinho

    @BeforeEach fun setup(){
        categoria = testEntityManager.persist(buildCategoria())
        produtos = testEntityManager.persist(buildProdutos(categoria = categoria))
        produtos2 = testEntityManager.persist(buildProdutos(nome = "X-bacon", categoria = categoria, quantidade = 4))
        carrinho = testEntityManager.persist(buildCarrinho(
            idProduto = produtos.id!!,
            nomeProduto = produtos.nome,
            quantidadeProduto = produtos.quantidade,
            precoProduto = produtos.precoUnitario
        ))
        testEntityManager.persist(buildCarrinho(
            idProduto = produtos2.id!!,
            nomeProduto = produtos2.nome,
            quantidadeProduto = produtos2.quantidade,
            precoProduto = produtos2.precoUnitario
        ))
    }

    @AfterEach
    fun teardown(){
        carrinhoRepository.deleteAll()
    }

    //SELECT, retorna todos os produtos do carrinho
    @Test
    fun `should return all produtos from carrinho`() {
        //when
        val carrinhoList: List<Carrinho> = carrinhoRepository.findAllProdutos()

        //then
        assertThat(carrinhoList).isNotEmpty
        assertThat(carrinhoList).hasSize(2)
    }

    //UPDATE, altera a quantidade e preco do produto no carrinho
    @Test
    fun `should update quantity and price in carrinho`() {
        //give
        val nomeProduto: String = produtos.nome
        val novaQuantidade = 5
        val novoPreco = BigDecimal("7.90")

        //when
        carrinhoRepository.update(nomeProduto, novaQuantidade, novoPreco)

        //then
        testEntityManager.flush()
        testEntityManager.clear()
        val carrinhoAtualizado: Carrinho = testEntityManager.find(Carrinho::class.java, carrinho.id)
        assertThat(carrinhoAtualizado).isNotNull
        assertThat(carrinhoAtualizado.quantidadeProduto).isEqualTo(novaQuantidade)
        assertThat(carrinhoAtualizado.precoProduto).isEqualTo(novoPreco)
    }

    //TRUNCATE, apaga DEFINITIVAMENTE tudo do carrinho
    @Test
    fun `should truncate carrinho table`() {
        //given
        val produtosNoCarrinho: List<Carrinho> = carrinhoRepository.findAllProdutos()
        assertThat(produtosNoCarrinho).isNotEmpty()

        //when
        carrinhoRepository.truncateAll()

        //then
        val produtosNoCarrinhoTruncate: List<Carrinho> = carrinhoRepository.findAllProdutos()
        assertThat(produtosNoCarrinhoTruncate).isEmpty()
    }

    @Test
    fun `should return product ID when exists in carrinho`() {
        //when
        val existsCarrinhoId: Long? = carrinhoRepository.existsCarrinho(produtos.id!!)

        // Then
        assertThat(existsCarrinhoId).isNotNull()
        assertThat(existsCarrinhoId).isEqualTo(produtos.id)
    }

    @Test
    fun `should return null when it does not exists in carrinho`() {
        //when
        val existsCarrinhoId: Long? = carrinhoRepository.existsCarrinho(500)

        //then
        assertThat(existsCarrinhoId).isNull()
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

    private fun buildCarrinho(
        idProduto: Long = 1L,
        nomeProduto: String = "X-salada",
        quantidadeProduto: Int = 3,
        precoProduto: BigDecimal = BigDecimal(5.50)
    ) = Carrinho(
        idProduto = idProduto,
        nomeProduto = nomeProduto,
        quantidadeProduto = quantidadeProduto,
        precoProduto = precoProduto
    )

}