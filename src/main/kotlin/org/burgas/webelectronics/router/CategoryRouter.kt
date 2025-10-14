package org.burgas.webelectronics.router

import org.burgas.webelectronics.dto.category.CategoryRequest
import org.burgas.webelectronics.exception.PartNotFoundException
import org.burgas.webelectronics.message.ImageMessages
import org.burgas.webelectronics.service.CategoryService
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
class CategoryRouter {

    private final val categoryService: CategoryService

    constructor(categoryService: CategoryService) {
        this.categoryService = categoryService
    }

    @Bean
    fun categoryRoutes(): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
            .GET("/api/v1/categories") {
                ServerResponse.ok().body(categoryService.findAll())
            }
            .GET("/api/v1/categories/by-id") {
                request ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                        this.categoryService.findById(
                            UUID.fromString(request.param("categoryId").orElseThrow())
                        )
                    )
            }
            .POST("/api/v1/categories/create-update") {
                request ->
                ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(this.categoryService.createOrUpdate(request.body<CategoryRequest>()))
            }
            .DELETE("/api/v1/categories/delete") {
                request -> this.categoryService.delete(UUID.fromString(request.param("categoryId").orElseThrow()))
                ServerResponse.noContent().build()
            }
            .POST("/api/v1/categories/create-image") {
                request ->
                this.categoryService.createImage(
                    UUID.fromString(request.param("categoryId").orElseThrow()),
                    request.multipartData().asSingleValueMap()["image"] ?:
                    throw PartNotFoundException(ImageMessages.PART_NOPT_FOUND.message)
                )
                ServerResponse.noContent().build()
            }
            .PUT("/api/v1/categories/change-image") {
                request ->
                this.categoryService.changeImage(
                    UUID.fromString(request.param("categoryId").orElseThrow()),
                    request.multipartData().asSingleValueMap()["image"] ?:
                    throw PartNotFoundException(ImageMessages.IMAGE_NOT_FOUND.message)
                )
                ServerResponse.noContent().build()
            }
            .DELETE("/api/v1/categories/delete-image") {
                request ->
                this.categoryService.deleteImage(
                    UUID.fromString(request.param("categoryId").orElseThrow())
                )
                ServerResponse.noContent().build()
            }
            .build()
    }
}