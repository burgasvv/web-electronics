package org.burgas.webelectronics.router

import org.burgas.webelectronics.dto.pk.BucketProductShortRequest
import org.burgas.webelectronics.filter.BucketFilterFunction
import org.burgas.webelectronics.service.BucketService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctions
import org.springframework.web.servlet.function.ServerResponse
import java.net.URI
import java.util.*

@Configuration
class BucketRouter {

    final val bucketService: BucketService
    final val bucketFilterFunction: BucketFilterFunction

    constructor(bucketService: BucketService, bucketFilterFunction: BucketFilterFunction) {
        this.bucketService = bucketService
        this.bucketFilterFunction = bucketFilterFunction
    }

    @Bean
    fun bucketRoutes(): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
            .filter(this.bucketFilterFunction)
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
                    request.attribute("bucketProductShortRequest").orElseThrow() as BucketProductShortRequest
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
                    request.attribute("bucketProductShortRequest").orElseThrow() as BucketProductShortRequest
                )
                ServerResponse
                    .status(HttpStatus.FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .location(URI.create("/api/v1/buckets/by-id?bucketId=$bucketId"))
                    .body(bucketId)
            }
            .DELETE("/api/v1/buckets/remove-all-products") {
                request ->
                val bucketId = this.bucketService.removeAllProducts(
                    UUID.fromString(request.param("bucketId").orElseThrow())
                )
                ServerResponse
                    .status(HttpStatus.FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .location(URI.create("/api/v1/buckets/by-id?bucketId=$bucketId"))
                    .body(bucketId)
            }
            .POST("/api/v1/buckets/increase-product") {
                request ->
                val bucketId = this.bucketService.increaseProduct(
                    request.attribute("bucketProductShortRequest").orElseThrow() as BucketProductShortRequest
                )
                ServerResponse
                    .status(HttpStatus.FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .location(URI.create("/api/v1/buckets/by-id?bucketId=$bucketId"))
                    .body(bucketId)
            }
            .POST("/api/v1/buckets/decrease-product") {
                request ->
                val bucketId = this.bucketService.decreaseProduct(
                    request.attribute("bucketProductShortRequest").orElseThrow() as BucketProductShortRequest
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