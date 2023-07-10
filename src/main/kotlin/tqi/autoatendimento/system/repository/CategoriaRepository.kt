package tqi.autoatendimento.system.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tqi.autoatendimento.system.entity.Categoria

@Repository
interface CategoriaRepository: JpaRepository<Categoria, Long> {
}