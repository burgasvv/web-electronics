package org.burgas.webelectronics.dto.product

import org.burgas.webelectronics.dto.Request
import java.util.*

data class ProductRequest(
    val id: UUID? = null,
    val categoryId: UUID? = null,
    val name: String? = null,
    val description: String? = null,
    val price: Double? = null
) : Request()
