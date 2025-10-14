package org.burgas.webelectronics.dto.store

import org.burgas.webelectronics.dto.Request
import org.burgas.webelectronics.entity.address.Address
import java.util.UUID

data class StoreRequest(
    val id: UUID? = null,
    val address: Address? = null,
) : Request()
