package org.burgas.webelectronics.dto.product

import org.burgas.webelectronics.dto.Response
import org.burgas.webelectronics.dto.category.CategoryShortResponse
import java.util.UUID

data class ProductShortResponse(
    val id: UUID? = null,
    val category: CategoryShortResponse? = null,
    val name: String? = null,
    val description: String? = null,
    val price: Double? = null
) : Response()
