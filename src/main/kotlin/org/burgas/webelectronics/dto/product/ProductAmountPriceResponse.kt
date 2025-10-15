package org.burgas.webelectronics.dto.product

import org.burgas.webelectronics.dto.Response

data class ProductAmountPriceResponse(
    val product: ProductShortResponse? = null,
    val amount: Long? = null,
    val price: Double? = null
) : Response()
