package tqi.autoatendimento.system.configuration

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Swagger3Config {

    /*
    Swagger para a documentação
    localhost:8080/swagger-ui.html
     */
    @Bean
    fun publicApi(): GroupedOpenApi?{
        return GroupedOpenApi.builder()
            .group("springcreditapplicationsystem-public")
            .pathsToMatch("/api/categoria/**", "/api/produtos/**", "/api/carrinho/**", "/api/finalizacao/**")
            .build()
    }

}