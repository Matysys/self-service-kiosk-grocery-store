package tqi.autoatendimento.system.service

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import tqi.autoatendimento.system.repository.ProdutosRepository
import tqi.autoatendimento.system.service.impl.ProdutosService
import org.assertj.core.api.Assertions.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class ProdutosServiceTest {
    @MockK lateinit var produtosRepository: ProdutosRepository
    @InjectMockKs lateinit var produtosService: ProdutosService

    @Test
    fun `should save categoria`(){
        //given

        //when

        //then
    }

}