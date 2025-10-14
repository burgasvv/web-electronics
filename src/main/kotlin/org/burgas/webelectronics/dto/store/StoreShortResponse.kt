package org.burgas.webelectronics.dto.store

import org.burgas.webelectronics.dto.Response
import org.burgas.webelectronics.entity.address.Address
import java.util.UUID

data class StoreShortResponse(
    val id: UUID? = null,
    val address: Address? = null
) : Response()
