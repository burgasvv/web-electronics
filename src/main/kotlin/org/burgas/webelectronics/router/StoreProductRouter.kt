package org.burgas.webelectronics.router

import org.burgas.webelectronics.dto.pk.StoreProductFullRequest
import org.burgas.webelectronics.dto.pk.StoreProductShortRequest
import org.burgas.webelectronics.service.StoreProductService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctions
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

@Configuration
class StoreProductRouter {

    final val storeProductService: StoreProductService

    constructor(storeProductService: StoreProductService) {
        this.storeProductService = storeProductService
    }

    @Bean
    fun storeProductRoutes(): RouterFunction<ServerResponse> {
        return RouterFunctions.route()
            .POST("/api/v1/store-product/add") {
                request ->
                ServerResponse.ok().body(
                    this.storeProductService
                        .add(request.body<StoreProductFullRequest>())
                )
            }
            .DELETE("/api/v1/store-product/delete") {
                request -> this.storeProductService.delete(request.body<StoreProductShortRequest>())
                ServerResponse.noContent().build()
            }
            .build()
    }
}