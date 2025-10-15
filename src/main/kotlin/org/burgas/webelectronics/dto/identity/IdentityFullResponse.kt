package org.burgas.webelectronics.dto.identity

import org.burgas.webelectronics.dto.Response
import org.burgas.webelectronics.dto.bucket.BucketShortResponse
import org.burgas.webelectronics.entity.identity.Authority
import org.burgas.webelectronics.entity.image.Image
import java.util.UUID

data class IdentityFullResponse(
    val id: UUID? = null,
    val authority: Authority? = null,
    val email: String? = null,
    val pass: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val patronymic: String? = null,
    val enabled: Boolean? = null,
    val image: Image? = null,
    val bucket: BucketShortResponse? = null
) : Response()
