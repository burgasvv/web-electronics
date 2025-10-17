package org.burgas.webelectronics.filter

import org.burgas.webelectronics.dto.pk.BucketProductShortRequest
import org.burgas.webelectronics.entity.identity.Identity
import org.burgas.webelectronics.exception.IdentityNotAuthenticatedException
import org.burgas.webelectronics.exception.IdentityNotAuthorizedException
import org.burgas.webelectronics.exception.IdentityNotFoundException
import org.burgas.webelectronics.message.IdentityMessages
import org.burgas.webelectronics.service.BucketService
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.*
import java.util.*

@Component
class BucketFilterFunction : HandlerFilterFunction<ServerResponse, ServerResponse> {

    private final val bucketService: BucketService

    constructor(bucketService: BucketService) {
        this.bucketService = bucketService
    }

    override fun filter(
        request: ServerRequest,
        next: HandlerFunction<ServerResponse?>
    ): ServerResponse {

        if (
            request.path().equals("/api/v1/buckets/by-id", ignoreCase = false) ||
            request.path().equals("/api/v1/buckets/remove-all-products", ignoreCase = false)
        ) {
            val authentication = request.principal()
                .map { principal -> Authentication::class.java.cast(principal as Authentication) }
                .orElseThrow()

            if (authentication.isAuthenticated) {
                val identity = authentication.principal as Identity
                val bucketIdParam = request.param("bucketId").orElseThrow()
                val bucketId = UUID.fromString(bucketIdParam)
                val bucket = this.bucketService.findEntity(bucketId)
                val bucketIdentity = bucket.identity
                    ?: throw IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.message)

                if (identity.id == bucketIdentity.id) {
                    return next.handle(request)

                } else {
                    throw IdentityNotAuthorizedException(IdentityMessages.NOT_AUTHORIZED.message)
                }

            } else {
                throw IdentityNotAuthenticatedException(IdentityMessages.NOT_AUTHENTICATED.message)
            }

        } else if (
            request.path().equals("/api/v1/buckets/add-product", ignoreCase = false) ||
            request.path().equals("/api/v1/buckets/remove-product", ignoreCase = false) ||
            request.path().equals("/api/v1/buckets/remove-all-products", ignoreCase = false) ||
            request.path().equals("/api/v1/buckets/increase-product", ignoreCase = false) ||
            request.path().equals("/api/v1/buckets/decrease-product", ignoreCase = false)
        ) {
            val authentication = request.principal()
                .map { principal -> Authentication::class.java.cast(principal as Authentication) }
                .orElseThrow()

            if (authentication.isAuthenticated) {
                val identity = authentication.principal as Identity
                val bucketProductShortRequest = request.body<BucketProductShortRequest>()
                val bucket = this.bucketService.findEntity(bucketProductShortRequest.bucketId)
                val bucketIdentity = bucket.identity
                    ?: throw IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.message)

                if (identity.id == bucketIdentity.id) {
                    request.attributes()["bucketProductShortRequest"] = bucketProductShortRequest
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