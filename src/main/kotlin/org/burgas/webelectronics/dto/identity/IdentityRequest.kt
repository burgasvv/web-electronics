package org.burgas.webelectronics.dto.identity

import org.burgas.webelectronics.dto.Request
import org.burgas.webelectronics.entity.identity.Authority
import java.util.UUID

data class IdentityRequest(
    val id: UUID?,
    val authority: Authority?,
    val email: String?,
    val pass: String?,
    val firstname: String?,
    val lastname: String?,
    val patronymic: String?,
    val enabled: Boolean?,
) : Request()