package tqi.autoatendimento.system.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.SQLIntegrityConstraintViolationException

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException::class)
    fun handlerValidException(ex: SQLIntegrityConstraintViolationException): ResponseEntity<String>{
        val exception: String = "Não é possível apagar a categoria da qual ainda há produtos."
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception)
    }

}