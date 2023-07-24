package tqi.autoatendimento.system.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tqi.autoatendimento.system.entity.Categoria

//Queries para a tabela 'categoria'
@Repository
interface CategoriaRepository: JpaRepository<Categoria, Long> {

/*
Não há métodos personalizados aqui, somente os próprios do JPA
 */

}