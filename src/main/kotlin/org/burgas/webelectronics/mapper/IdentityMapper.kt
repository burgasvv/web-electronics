package org.burgas.webelectronics.mapper

import org.burgas.webelectronics.dto.identity.IdentityFullResponse
import org.burgas.webelectronics.dto.identity.IdentityRequest
import org.burgas.webelectronics.dto.identity.IdentityShortResponse
import org.burgas.webelectronics.entity.identity.Identity
import org.burgas.webelectronics.exception.FieldEmptyException
import org.burgas.webelectronics.message.IdentityMessages.*
import org.burgas.webelectronics.repository.IdentityRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
class IdentityMapper : EntityMapper<IdentityRequest, Identity, IdentityShortResponse, IdentityFullResponse> {

    private final val identityRepository: IdentityRepository
    private final val passwordEncoder: PasswordEncoder
    private final val bucketMapperObjectFactory: ObjectFactory<BucketMapper>

    constructor(
        identityRepository: IdentityRepository,
        passwordEncoder: PasswordEncoder,
        bucketMapperObjectFactory: ObjectFactory<BucketMapper>
    ) {
        this.identityRepository = identityRepository
        this.passwordEncoder = passwordEncoder
        this.bucketMapperObjectFactory = bucketMapperObjectFactory
    }

    private fun getBucketMapper(): BucketMapper {
        return bucketMapperObjectFactory.`object`
    }

    override fun toEntity(request: IdentityRequest): Identity {
        val identityId = request.id ?: UUID.randomUUID()
        return this.identityRepository.findById(identityId)
            .map { identity ->
                val authority = request.authority ?: identity.authority
                val email = request.email ?: identity.email
                val firstname = request.firstname ?: identity.firstname
                val lastname = request.lastname ?: identity.lastname
                val patronymic = request.patronymic ?: identity.patronymic
                Identity().apply {
                    this.id = identity.id
                    this.authority = authority
                    this.email = email
                    this.pass = identity.pass
                    this.firstname = firstname
                    this.lastname = lastname
                    this.patronymic = patronymic
                    this.enabled = identity.enabled
                    this.bucket = identity.bucket
                }
            }
            .orElseGet {
                val authority = request.authority ?: throw FieldEmptyException(AUTHORITY_FIELD_EMPTY.message)
                val email = request.email ?: throw FieldEmptyException(EMAIL_FIELD_EMPTY.message)
                val pass = request.pass ?: throw FieldEmptyException(PASSWORD_FIELD_EMPTY.message)
                val firstname = request.firstname ?: throw FieldEmptyException(FIRST_NAME_FIELD_EMPTY.message)
                val lastname = request.lastname ?: throw FieldEmptyException(LAST_NAME_FIELD_EMPTY.message)
                val patronymic = request.patronymic ?: throw FieldEmptyException(PATRONYMIC_FIELD_EMPTY.message)
                Identity().apply {
                    this.authority = authority
                    this.email = email
                    this.pass = passwordEncoder.encode(pass)
                    this.firstname = firstname
                    this.lastname = lastname
                    this.patronymic = patronymic
                    this.enabled = true
                }
            }
    }

    override fun toShortResponse(entity: Identity): IdentityShortResponse {
        return IdentityShortResponse(
            id = entity.id,
            authority = entity.authority,
            email = entity.email,
            pass = entity.pass,
            firstname = entity.firstname,
            lastname = entity.lastname,
            patronymic = entity.patronymic,
            enabled = entity.enabled,
            image = entity.image
        )
    }

    override fun toFullResponse(entity: Identity): IdentityFullResponse {
        return IdentityFullResponse(
            id = entity.id,
            authority = entity.authority,
            email = entity.email,
            pass = entity.pass,
            firstname = entity.firstname,
            lastname = entity.lastname,
            patronymic = entity.patronymic,
            enabled = entity.enabled,
            image = entity.image,
            bucket = Optional.ofNullable(entity.bucket)
                .map { bucket -> this.getBucketMapper().toShortResponse(bucket) }
                .orElse(null)
        )
    }
}