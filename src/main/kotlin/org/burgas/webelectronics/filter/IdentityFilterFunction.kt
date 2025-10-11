package org.burgas.webelectronics.filter

import org.burgas.webelectronics.dto.identity.IdentityRequest
import org.burgas.webelectronics.entity.identity.Identity
import org.burgas.webelectronics.exception.IdentityNotAuthenticatedException
import org.burgas.webelectronics.exception.IdentityNotAuthorizedException
import org.burgas.webelectronics.message.IdentityMessages
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.HandlerFilterFunction
import org.springframework.web.servlet.function.HandlerFunction
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import java.util.UUID

@Component
class IdentityFilterFunction : HandlerFilterFunction<ServerResponse, ServerResponse> {

    override fun filter(
        request: ServerRequest,
        next: HandlerFunction<ServerResponse?>
    ): ServerResponse {

        if (
            request.path().equals("/api/v1/identities/by-id", ignoreCase = false) ||
            request.path().equals("/api/v1/identities/delete", ignoreCase = false) ||
            request.path().equals("/api/v1/identities/change-password", ignoreCase = false) ||
            request.path().equals("/api/v1/identities/create-image", ignoreCase = false) ||
            request.path().equals("/api/v1/identities/change-image", ignoreCase = false) ||
            request.path().equals("/api/v1/identities/delete-image", ignoreCase = false)
        ) {
            val authentication = request.principal()
                .map { principal -> Authentication::class.java.cast(principal) }
                .orElseThrow()

            if (authentication.isAuthenticated) {
                val identity = authentication.principal as Identity
                val identityIdParam = request.param("identityId").orElseThrow()
                val identityId = UUID.fromString(identityIdParam)

                if (identity.id == identityId) {
                    return next.handle(request)

                } else {
                    throw IdentityNotAuthorizedException(IdentityMessages.NOT_AUTHORIZED.message)
                }

            } else {
                throw IdentityNotAuthenticatedException(IdentityMessages.NOT_AUTHENTICATED.message)
            }

        } else if (
            request.path().equals("/api/v1/identities/update", ignoreCase = false)
        ) {
            val authentication = request.principal()
                .map { principal -> Authentication::class.java.cast(principal) }
                .orElseThrow()

            if (authentication.isAuthenticated) {
                val identity = authentication.principal as Identity
                val identityRequest = request.body<IdentityRequest>()

                if (identity.id == identityRequest.id) {
                    request.attributes()["identityRequest"] = identityRequest
                    return next.handle(request)

                } else {
                    throw IdentityNotAuthorizedException(IdentityMessages.NOT_AUTHORIZED.message)
                }

            } else {
                throw IdentityNotAuthenticatedException(IdentityMessages.NOT_AUTHENTICATED.message)
            }
        }

        return next.handle(request)
    }
}