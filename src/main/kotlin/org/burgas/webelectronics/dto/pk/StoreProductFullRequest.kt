package org.burgas.webelectronics.dto.pk

import org.burgas.webelectronics.dto.Request
import java.util.UUID

data class StoreProductFullRequest(
    val storeId: UUID,
    val productId: UUID,
    val amount: Long
) : Request()
