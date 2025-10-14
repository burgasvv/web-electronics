package org.burgas.webelectronics.dto.store

import org.burgas.webelectronics.dto.Response
import org.burgas.webelectronics.dto.product.ProductAmountResponse
import org.burgas.webelectronics.entity.address.Address
import java.util.*

data class StoreFullResponse(
    val id: UUID? = null,
    val address: Address? = null,
    val products: List<ProductAmountResponse> = mutableListOf()
) : Response()
