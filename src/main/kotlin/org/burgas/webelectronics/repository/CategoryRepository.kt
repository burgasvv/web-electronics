package org.burgas.webelectronics.repository

import org.burgas.webelectronics.entity.category.Category
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface CategoryRepository : JpaRepository<Category, UUID> {

    @EntityGraph(value = "category-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findAll(): List<Category>

    @EntityGraph(value = "category-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findById(id: UUID): Optional<Category>
}