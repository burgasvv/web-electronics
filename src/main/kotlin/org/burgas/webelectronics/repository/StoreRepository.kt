package org.burgas.webelectronics.repository

import org.burgas.webelectronics.entity.store.Store
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface StoreRepository : JpaRepository<Store, UUID> {

    @EntityGraph(value = "store-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findAll(): List<Store>

    @EntityGraph(value = "store-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findById(id: UUID): Optional<Store>
}