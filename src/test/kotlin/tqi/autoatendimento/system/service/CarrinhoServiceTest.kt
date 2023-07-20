package tqi.autoatendimento.system.service

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import tqi.autoatendimento.system.repository.CarrinhoRepository
import tqi.autoatendimento.system.service.impl.CarrinhoService
import org.assertj.core.api.Assertions.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CarrinhoServiceTest {
    @MockK lateinit var carrinhoRepository: CarrinhoRepository
    @InjectMockKs lateinit var carrinhoService: CarrinhoService
    /*
    @Test
    fun `should save categoria`(){
        //given

        //when

        //then
    }*/
}