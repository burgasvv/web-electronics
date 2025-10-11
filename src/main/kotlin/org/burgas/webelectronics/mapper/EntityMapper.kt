package org.burgas.webelectronics.mapper

import org.burgas.webelectronics.dto.Request
import org.burgas.webelectronics.dto.Response
import org.burgas.webelectronics.entity.BaseEntity
import org.burgas.webelectronics.exception.FieldEmptyException
import org.springframework.stereotype.Component

@Component
interface EntityMapper<R : Request, E : BaseEntity, S : Response, F : Response> {

    fun <D> handleData(requestData: D, entityData: D): D {
        return if (requestData == null || requestData == "") entityData else requestData
    }

    fun <D> handleDataThrowable(requestData: D, message: String): D {
        return if (requestData == null || requestData == "") throw FieldEmptyException(message) else requestData
    }

    fun toEntity(request: R): E

    fun toShortResponse(entity: E): S

    fun toFullResponse(entity: E): F
}