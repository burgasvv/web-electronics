package org.burgas.webelectronics.router

import org.burgas.webelectronics.dto.store.StoreRequest
import org.burgas.webelectronics.service.StoreService
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
class StoreRouter {

    private final val storeService: StoreService

    constructor(storeService: StoreService) {
        this.storeService = storeService
    }

    @Bean
    fun storeRoutes(): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
            .GET("/api/v1/stores") {
                _ ->
                ServerResponse.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(this.storeService.findAll())
            }
            .GET("/api/v1/stores/by-id") {
                request ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                        this.storeService.findById(
                            UUID.fromString(request.param("storeId").orElseThrow())
                        )
                    )
            }
            .POST("/api/v1/stores/create-update") {
                request ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(this.storeService.createOrUpdate(request.body<StoreRequest>()))
            }
            .DELETE("/api/v1/stores/delete") {
                request -> this.storeService.delete(UUID.fromString(request.param("storeId").orElseThrow()))
                return@DELETE ServerResponse.noContent().build()
            }
            .build()
    }
}