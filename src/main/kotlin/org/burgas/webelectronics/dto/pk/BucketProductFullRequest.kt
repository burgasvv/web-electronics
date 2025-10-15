package org.burgas.webelectronics.dto.pk

import org.burgas.webelectronics.dto.Request
import java.util.UUID

data class BucketProductFullRequest(
    val bucketId: UUID,
    val productId: UUID,
    val amount: Long,
    val price: Double
) : Request()
