package org.burgas.webelectronics.dto.pk

import org.burgas.webelectronics.dto.Request
import java.util.UUID

data class BucketProductShortRequest(
    val bucketId: UUID,
    val productId: UUID,
    val amount: Long? = null
) : Request()
