package tqi.autoatendimento.system.service

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import tqi.autoatendimento.system.repository.PedidosRepository
import tqi.autoatendimento.system.service.impl.PedidosService
import org.assertj.core.api.Assertions.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class PedidosServiceTest {
    @MockK lateinit var pedidosRepository: PedidosRepository
    @InjectMockKs lateinit var pedidosService: PedidosService

    @Test
    fun `should save categoria`(){
        //given

        //when

        //then
    }
}