package org.burgas.webelectronics.repository

import org.burgas.webelectronics.entity.product.Product
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface ProductRepository : JpaRepository<Product, UUID> {

    @EntityGraph(value = "product-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findAll(): List<Product>

    @EntityGraph(value = "product-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findById(id: UUID): Optional<Product>
}