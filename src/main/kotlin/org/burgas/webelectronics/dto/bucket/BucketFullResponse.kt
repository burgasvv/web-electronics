package org.burgas.webelectronics.dto.bucket

import org.burgas.webelectronics.dto.Response
import org.burgas.webelectronics.dto.identity.IdentityShortResponse
import org.burgas.webelectronics.dto.product.ProductAmountPriceResponse
import java.util.*

data class BucketFullResponse(
    val id: UUID? = null,
    val identity: IdentityShortResponse? = null,
    val balance: Double? = null,
    val products: List<ProductAmountPriceResponse> = mutableListOf()
) : Response()
