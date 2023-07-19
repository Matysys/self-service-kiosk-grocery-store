package tqi.autoatendimento.system.exception

import jakarta.validation.UnexpectedTypeException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.SQLIntegrityConstraintViolationException

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException::class)
    fun handlerSQLException(ex: SQLIntegrityConstraintViolationException): ResponseEntity<String>{
        val exception: String = "Não é possível apagar a categoria da qual ainda há produtos."
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception)
    }
    /*
    @ExceptionHandler(UnexpectedTypeException::class)
    fun handlerValidException(ex: UnexpectedTypeException): ResponseEntity<String>{
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Informações inválidas")
    }*/

}