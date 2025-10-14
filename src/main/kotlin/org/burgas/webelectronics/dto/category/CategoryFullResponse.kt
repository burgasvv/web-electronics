package org.burgas.webelectronics.dto.category

import org.burgas.webelectronics.dto.Response
import org.burgas.webelectronics.dto.product.ProductShortResponse
import org.burgas.webelectronics.entity.image.Image
import java.util.UUID

data class CategoryFullResponse(
    val id: UUID? = null,
    val name: String? = null,
    val description: String? = null,
    val image: Image? = null,
    val products: List<ProductShortResponse> = mutableListOf()
) : Response()
