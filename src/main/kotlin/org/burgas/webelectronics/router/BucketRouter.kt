package org.burgas.webelectronics.router

import org.burgas.webelectronics.dto.pk.BucketProductShortRequest
import org.burgas.webelectronics.service.BucketService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctions
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import java.net.URI
import java.util.UUID

@Configuration
class BucketRouter {

    final val bucketService: BucketService

    constructor(bucketService: BucketService) {
        this.bucketService = bucketService
    }

    @Bean
    fun bucketRoutes(): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
            .GET("/api/v1/buckets/by-id") {
                request ->
                ServerResponse.ok().body(
                    this.bucketService.findById(
                        UUID.fromString(request.param("bucketId").orElseThrow())
                    )
                )
            }
            .POST("/api/v1/buckets/add-product") {
                request ->
                val bucketId = this.bucketService.addProduct(
                    request.body<BucketProductShortRequest>()
                )
                ServerResponse
                    .status(HttpStatus.FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .location(URI.create("/api/v1/buckets/by-id?bucketId=$bucketId"))
                    .body(bucketId)
            }
            .DELETE("/api/v1/buckets/remove-product") {
                request ->
                val bucketId = this.bucketService.removeProduct(
                    request.body<BucketProductShortRequest>()
                )
                ServerResponse
                    .status(HttpStatus.FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .location(URI.create("/api/v1/buckets/by-id?bucketId=$bucketId"))
                    .body(bucketId)
            }
            .build()
    }
}