package org.burgas.webelectronics.router

import org.burgas.webelectronics.dto.product.ProductRequest
import org.burgas.webelectronics.exception.PartNotFoundException
import org.burgas.webelectronics.message.ImageMessages
import org.burgas.webelectronics.service.ProductService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctions
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import java.util.UUID

@Configuration
class ProductRouter {

    private final val productService: ProductService

    constructor(productService: ProductService) {
        this.productService = productService
    }

    @Bean
    fun productRoutes(): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
            .GET("/api/v1/products") {
                ServerResponse.ok().body(productService.findAll())
            }
            .GET("/api/v1/products/by-id") {
                request ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                        this.productService.findById(
                            UUID.fromString(request.param("productId").orElseThrow())
                        )
                    )
            }
            .POST("/api/v1/products/create-update") {
                request ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                        this.productService.createOrUpdate(request.body<ProductRequest>())
                    )
            }
            .DELETE("/api/v1/products/delete") {
                request -> this.productService.delete(UUID.fromString(request.param("productId").orElseThrow()))
                ServerResponse.noContent().build()
            }
            .POST("/api/v1/products/create-image") {
                request ->
                this.productService.createImage(
                    UUID.fromString(request.param("productId").orElseThrow()),
                    request.multipartData().asSingleValueMap()["image"] ?:
                    throw PartNotFoundException(ImageMessages.PART_NOPT_FOUND.message)
                )
                ServerResponse.noContent().build()
            }
            .PUT("/api/v1/products/change-image") {
                request ->
                this.productService.changeImage(
                    UUID.fromString(request.param("productId").orElseThrow()),
                    request.multipartData().asSingleValueMap()["image"] ?:
                    throw PartNotFoundException(ImageMessages.PART_NOPT_FOUND.message)
                )
                ServerResponse.noContent().build()
            }
            .DELETE("/api/v1/products/delete-image") {
                request -> this.productService.deleteImage(
                UUID.fromString(request.param("productId").orElseThrow())
                )
                ServerResponse.noContent().build()
            }
            .build()
    }
}