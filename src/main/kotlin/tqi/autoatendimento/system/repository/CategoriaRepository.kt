package tqi.autoatendimento.system.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import tqi.autoatendimento.system.entity.Categoria

@Repository
interface CategoriaRepository: JpaRepository<Categoria, Long> {

    @Query("SELECT * FROM categoria WHERE nome = :nome", nativeQuery = true)
    fun existsByName(@Param("nome") nome: String): Int

}