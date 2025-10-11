package org.burgas.webelectronics.mapper

import org.burgas.webelectronics.dto.identity.IdentityFullResponse
import org.burgas.webelectronics.dto.identity.IdentityRequest
import org.burgas.webelectronics.dto.identity.IdentityShortResponse
import org.burgas.webelectronics.entity.identity.Authority
import org.burgas.webelectronics.entity.identity.Identity
import org.burgas.webelectronics.message.IdentityMessages.*
import org.burgas.webelectronics.repository.IdentityRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
class IdentityMapper : EntityMapper<IdentityRequest, Identity, IdentityShortResponse, IdentityFullResponse> {

    final val identityRepository: IdentityRepository
    final val passwordEncoder: PasswordEncoder

    constructor(identityRepository: IdentityRepository, passwordEncoder: PasswordEncoder) {
        this.identityRepository = identityRepository
        this.passwordEncoder = passwordEncoder
    }

    override fun toEntity(request: IdentityRequest): Identity {
        val identityId = this.handleData(request.id, UUID.randomUUID()) as UUID
        val handleIdentity = Identity()
        return this.identityRepository.findById(identityId)
            .map { identity ->
                val authority = this.handleData(request.authority, identity.authority) as Authority
                val email = this.handleData(request.email, identity.email) as String
                val firstname = this.handleData(request.firstname, identity.firstname) as String
                val lastname = this.handleData(request.lastname, identity.lastname) as String
                val patronymic = this.handleData(request.patronymic, identity.patronymic) as String
                return@map handleIdentity.apply {
                    this.id = identity.id
                    this.authority = authority
                    this.email = email
                    this.pass = identity.pass
                    this.firstname = firstname
                    this.lastname = lastname
                    this.patronymic = patronymic
                    this.enabled = identity.enabled
                }
            }
            .orElseGet {
                val authority = this.handleDataThrowable(request.authority, AUTHORITY_FIELD_EMPTY.message) as Authority
                val email = this.handleDataThrowable(request.email, EMAIL_FIELD_EMPTY.message) as String
                val pass = this.handleDataThrowable(request.pass, PASSWORD_FIELD_EMPTY.message) as String
                val firstname = this.handleDataThrowable(request.firstname, FIRST_NAME_FIELD_EMPTY.message) as String
                val lastname = this.handleDataThrowable(request.lastname, LAST_NAME_FIELD_EMPTY.message) as String
                val patronymic = this.handleDataThrowable(request.patronymic, PATRONYMIC_FIELD_EMPTY.message) as String
                return@orElseGet handleIdentity.apply {
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
            image = entity.image
        )
    }
}