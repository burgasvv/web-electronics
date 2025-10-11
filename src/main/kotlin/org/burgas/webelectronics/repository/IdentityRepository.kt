package org.burgas.webelectronics.repository

import org.burgas.webelectronics.entity.identity.Identity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface IdentityRepository : JpaRepository<Identity, UUID> {

    @EntityGraph(value = "identity-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findById(id: UUID): Optional<Identity>

    @EntityGraph(value = "identity-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findAll(): List<Identity>

    fun findIdentityByEmail(email: String): Optional<Identity>
}