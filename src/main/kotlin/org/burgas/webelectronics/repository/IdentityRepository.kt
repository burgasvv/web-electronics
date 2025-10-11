package org.burgas.webelectronics.repository

import org.burgas.webelectronics.entity.identity.Identity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface IdentityRepository : JpaRepository<Identity, UUID> {

    override fun findById(id: UUID): Optional<Identity>

    fun findIdentityByEmail(email: String): Optional<Identity>
}