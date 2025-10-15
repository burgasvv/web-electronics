package org.burgas.webelectronics.dto.product

import org.burgas.webelectronics.dto.Response
import org.burgas.webelectronics.dto.category.CategoryShortResponse
import org.burgas.webelectronics.dto.store.StoreShortResponse
import org.burgas.webelectronics.entity.image.Image
import java.util.*

data class ProductFullResponse(
    val id: UUID? = null,
    val category: CategoryShortResponse? = null,
    val name: String? = null,
    val description: String? = null,
    val price: Double? = null,
    val image: Image? = null,
    val stores: List<StoreShortResponse> = mutableListOf()
) : Response()
