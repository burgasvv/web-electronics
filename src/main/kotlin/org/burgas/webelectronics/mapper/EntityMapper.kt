package org.burgas.webelectronics.mapper

import org.burgas.webelectronics.dto.Request
import org.burgas.webelectronics.dto.Response
import org.burgas.webelectronics.entity.BaseEntity
import org.springframework.stereotype.Component

@Component
interface EntityMapper<R : Request, E : BaseEntity, S : Response, F : Response> {

    fun toEntity(request: R): E

    fun toShortResponse(entity: E): S

    fun toFullResponse(entity: E): F
}