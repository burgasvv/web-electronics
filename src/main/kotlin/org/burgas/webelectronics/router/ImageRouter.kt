package org.burgas.webelectronics.router

import jakarta.servlet.http.Part
import org.burgas.webelectronics.service.ImageService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctions
import org.springframework.web.servlet.function.ServerResponse
import java.io.ByteArrayInputStream
import java.util.UUID

@Configuration
class ImageRouter {

    final val imageService: ImageService

    constructor(imageService: ImageService) {
        this.imageService = imageService
    }

    @Bean
    fun imageRoutes(): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
            .GET("/api/v1/images/by-id") {
                request ->
                val image = this.imageService.findEntity(
                    UUID.fromString(request.param("imageId").orElseThrow())
                )
                return@GET ServerResponse
                    .status(HttpStatus.OK)
                    .contentType(MediaType.parseMediaType(image.contentType))
                    .body(
                        InputStreamResource(
                            ByteArrayInputStream(image.data)
                        )
                    )
            }
            .POST("/api/v1/images/create") {
                request ->
                val part = request.multipartData().asSingleValueMap()["image"] as Part
                if (part.contentType.split("/").first() == "image") {
                    val image = this.imageService.createImage(part)
                    return@POST ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(image)

                } else {
                    throw RuntimeException("Wrong file part (need image)")
                }
            }
            .PUT("/api/v1/images/change") {
                request ->
                val part = request.multipartData().asSingleValueMap()["image"] as Part
                if (part.contentType.split("/").first() == "image") {
                    val image = this.imageService.changeImage(
                        UUID.fromString(request.param("imageId").orElseThrow()),
                        part
                    )
                    return@PUT ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(image)
                } else {
                    throw RuntimeException("Wrong file part (need image)")
                }
            }
            .DELETE("/api/v1/images/delete") {
                request -> this.imageService.deleteImage(UUID.fromString(request.param("imageId").orElseThrow()))
                return@DELETE ServerResponse.noContent().build()
            }
            .build()
    }
}