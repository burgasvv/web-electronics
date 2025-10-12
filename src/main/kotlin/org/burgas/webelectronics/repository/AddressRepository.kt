package org.burgas.webelectronics.repository

import org.burgas.webelectronics.entity.address.Address
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AddressRepository : JpaRepository<Address, UUID> {
}