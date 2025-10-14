package org.burgas.webelectronics.service

import jakarta.servlet.http.Part
import org.burgas.webelectronics.dto.identity.IdentityFullResponse
import org.burgas.webelectronics.dto.identity.IdentityRequest
import org.burgas.webelectronics.dto.identity.IdentityShortResponse
import org.burgas.webelectronics.entity.identity.Identity
import org.burgas.webelectronics.exception.*
import org.burgas.webelectronics.mapper.IdentityMapper
import org.burgas.webelectronics.message.IdentityMessages
import org.burgas.webelectronics.message.ImageMessages
import org.burgas.webelectronics.repository.IdentityRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class IdentityService : CrudService<IdentityRequest, Identity, IdentityShortResponse, IdentityFullResponse> {

    private final val identityRepository: IdentityRepository
    private final val identityMapper: IdentityMapper
    private final val imageServiceObjectFactory: ObjectFactory<ImageService>

    constructor(
        identityRepository: IdentityRepository,
        identityMapper: IdentityMapper,
        imageServiceObjectFactory: ObjectFactory<ImageService>
    ) {
        this.identityRepository = identityRepository
        this.identityMapper = identityMapper
        this.imageServiceObjectFactory = imageServiceObjectFactory
    }

    private fun getImageService(): ImageService {
        return this.imageServiceObjectFactory.`object`
    }

    override fun findEntity(id: UUID): Identity {
        return this.identityRepository.findById(id)
            .orElseThrow { throw IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.message) }
    }

    override fun findAll(): List<IdentityShortResponse> {
        return this.identityRepository.findAll()
            .map { identity -> this.identityMapper.toShortResponse(identity) }
            .toList()
    }

    override fun findById(id: UUID): IdentityFullResponse {
        return this.identityMapper.toFullResponse(this.findEntity(id))
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    override fun createOrUpdate(request: IdentityRequest): IdentityFullResponse {
        return this.identityMapper.toFullResponse(
            identityRepository.save(this.identityMapper.toEntity(request))
        )
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    override fun delete(id: UUID) {
        val identity = this.findEntity(id)
        this.identityRepository.delete(identity)
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun changePassword(identityId: UUID, newPassword: String) {
        if (newPassword.isBlank()) throw PasswordIsEmptyException(IdentityMessages.PASSWORD_FIELD_EMPTY.message)
        val identity = this.findEntity(identityId)
        if (identity.pass != newPassword) {
            identity.pass = newPassword
        } else {
            throw SamePasswordException(IdentityMessages.SAME_PASSWORD.message)
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun enableDisable(identityId: UUID, enabled: Boolean) {
        val identity = this.findEntity(identityId)
        if (identity.enabled != enabled) {
            identity.enabled = enabled
        } else {
            throw AlreadyEnabledException(IdentityMessages.ENABLED_MATCHES.message)
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun createImage(identityId: UUID, part: Part) {
        val image = this.getImageService().createImage(part)
        val identity = this.findEntity(identityId)
        identity.apply {
            this.image = image
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun changeImage(identityId: UUID, part: Part) {
        val identity = this.findEntity(identityId)
        val image = identity.image ?: throw ImageNotFoundException(ImageMessages.IMAGE_NOT_FOUND.message)
        this.getImageService().changeImage(image.id, part)
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun deleteImage(identityId: UUID) {
        val identity = this.findEntity(identityId)
        val image = identity.image ?: throw ImageNotFoundException(ImageMessages.IMAGE_NOT_FOUND.message)
        identity.apply {
            this.image = null
        }
        this.getImageService().deleteImage(image.id)
    }
}