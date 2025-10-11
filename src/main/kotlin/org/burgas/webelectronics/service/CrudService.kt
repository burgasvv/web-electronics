package org.burgas.webelectronics.service

import org.burgas.webelectronics.dto.Request
import org.burgas.webelectronics.dto.Response
import org.burgas.webelectronics.entity.BaseEntity
import org.springframework.stereotype.Service
import java.util.UUID

@Service
interface CrudService<R : Request, E : BaseEntity, S : Response, F : Response> {

    fun findEntity(id: UUID): E

    fun findAll(): List<S>

    fun findById(id: UUID): F

    fun createOrUpdate(request: R): F

    fun delete(id: UUID)
}