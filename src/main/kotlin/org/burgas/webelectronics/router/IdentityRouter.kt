package org.burgas.webelectronics.router

import jakarta.servlet.http.Part
import org.burgas.webelectronics.dto.identity.IdentityRequest
import org.burgas.webelectronics.filter.IdentityFilterFunction
import org.burgas.webelectronics.service.IdentityService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctions
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import java.util.*

@Configuration
class IdentityRouter {

    final val identityService: IdentityService
    final val identityFilterFunction: IdentityFilterFunction

    constructor(identityService: IdentityService, identityFilterFunction: IdentityFilterFunction) {
        this.identityService = identityService
        this.identityFilterFunction = identityFilterFunction
    }

    @Bean
    fun identityRoutes(): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
            .filter(this.identityFilterFunction)
            .GET("/api/v1/identities") { _ ->
                ServerResponse.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(this.identityService.findAll())
            }
            .GET("/api/v1/identities/by-id") { request ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                        this.identityService.findById(
                            UUID.fromString(request.param("identityId").orElseThrow())
                        )
                    )
            }
            .POST("/api/v1/identities/create") { request ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                        this.identityService.createOrUpdate(request.body<IdentityRequest>())
                    )
            }
            .PUT("/api/v1/identities/update") { request ->
                @Suppress("CAST_NEVER_SUCCEEDS")
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                        this.identityService.createOrUpdate(
                            request.attribute("identityRequest").orElseThrow() as IdentityRequest
                        )
                    )
            }
            .DELETE("/api/v1/identities/delete") { request ->
                this.identityService.delete(UUID.fromString(request.param("identityId").orElseThrow()))
                return@DELETE ServerResponse.noContent().build()
            }
            .PUT("/api/v1/identities/change-password") { request ->
                this.identityService.changePassword(
                    UUID.fromString(request.param("identityId").orElseThrow()),
                    request.param("newPassword").orElseThrow()
                )
                return@PUT ServerResponse.noContent().build()
            }
            .PUT("/api/v1/identities/enable-disable") { request ->
                this.identityService.enableDisable(
                    UUID.fromString(request.param("identityId").orElseThrow()),
                    request.param("enabled").orElseThrow().toBoolean()
                )
                return@PUT ServerResponse.noContent().build()
            }
            .POST("/api/v1/identities/create-image") { request ->
                this.identityService.createImage(
                    UUID.fromString(request.param("identityId").orElseThrow()),
                    request.multipartData().asSingleValueMap()["image"] as Part
                )
                return@POST ServerResponse.noContent().build()
            }
            .PUT("/api/v1/identities/change-image") { request ->
                this.identityService.changeImage(
                    UUID.fromString(request.param("identityId").orElseThrow()),
                    request.multipartData().asSingleValueMap()["image"] as Part
                )
                return@PUT ServerResponse.noContent().build()
            }
            .DELETE("/api/v1/identities/delete-image") { request ->
                this.identityService.deleteImage(UUID.fromString(request.param("identityId").orElseThrow()))
                return@DELETE ServerResponse.noContent().build()
            }
            .build()
    }
}