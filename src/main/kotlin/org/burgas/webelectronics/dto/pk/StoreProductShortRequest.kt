package org.burgas.webelectronics.dto.pk

import org.burgas.webelectronics.dto.Request
import java.util.UUID

data class StoreProductShortRequest(
    val storeId: UUID,
    val productId: UUID
): Request()
