package org.burgas.webelectronics.dto.identity

import org.burgas.webelectronics.dto.Response
import org.burgas.webelectronics.entity.identity.Authority
import org.burgas.webelectronics.entity.image.Image
import java.util.UUID

data class IdentityShortResponse(
    val id: UUID?,
    val authority: Authority?,
    val email: String?,
    val pass: String?,
    val firstname: String?,
    val lastname: String?,
    val patronymic: String?,
    val enabled: Boolean?,
    val image: Image?
) : Response()
