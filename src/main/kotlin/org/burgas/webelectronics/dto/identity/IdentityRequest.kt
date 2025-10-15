package org.burgas.webelectronics.dto.identity

import org.burgas.webelectronics.dto.Request
import org.burgas.webelectronics.entity.identity.Authority
import java.util.UUID

data class IdentityRequest(
    val id: UUID? = null,
    val authority: Authority? = null,
    val email: String? = null,
    val pass: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val patronymic: String? = null,
    val enabled: Boolean? = null
) : Request()