package org.burgas.webelectronics.dto.product

import org.burgas.webelectronics.dto.Response

data class ProductAmountResponse(
    val product: ProductShortResponse,
    val amount: Long
) : Response()
