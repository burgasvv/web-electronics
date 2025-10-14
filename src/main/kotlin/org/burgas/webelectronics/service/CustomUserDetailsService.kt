package org.burgas.webelectronics.service

import org.burgas.webelectronics.exception.IdentityNotFoundException
import org.burgas.webelectronics.message.IdentityMessages
import org.burgas.webelectronics.repository.IdentityRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService : UserDetailsService {

    private final val identityRepository: IdentityRepository

    constructor(identityRepository: IdentityRepository) {
        this.identityRepository = identityRepository
    }

    override fun loadUserByUsername(username: String?): UserDetails? {
        return this.identityRepository.findIdentityByEmail(username as String)
            .orElseThrow { throw IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.message) }
    }
}