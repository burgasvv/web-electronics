package org.burgas.webelectronics.dto.category

import org.burgas.webelectronics.dto.Request
import java.util.UUID

data class CategoryRequest(
    val id: UUID? = null,
    val name: String? = null,
    val description: String? = null
) : Request()
