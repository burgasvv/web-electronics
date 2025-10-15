package org.burgas.webelectronics.dto.bucket

import org.burgas.webelectronics.dto.Response
import java.util.UUID

data class BucketShortResponse(
    val id: UUID? = null,
    val balance: Double? = null
) : Response()
