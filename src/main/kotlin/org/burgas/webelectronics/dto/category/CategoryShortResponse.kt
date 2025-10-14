package org.burgas.webelectronics.dto.category

import org.burgas.webelectronics.dto.Response
import org.burgas.webelectronics.entity.image.Image
import java.util.UUID

data class CategoryShortResponse(
    val id: UUID? = null,
    val name: String? = null,
    val description: String? = null,
    val image: Image? = null
) : Response()
