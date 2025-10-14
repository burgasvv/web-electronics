package org.burgas.webelectronics.mapper

import org.burgas.webelectronics.dto.product.ProductAmountResponse
import org.burgas.webelectronics.dto.store.StoreFullResponse
import org.burgas.webelectronics.dto.store.StoreRequest
import org.burgas.webelectronics.dto.store.StoreShortResponse
import org.burgas.webelectronics.entity.product.Product
import org.burgas.webelectronics.entity.store.Store
import org.burgas.webelectronics.repository.StoreRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class StoreMapper : EntityMapper<StoreRequest, Store, StoreShortResponse, StoreFullResponse> {

    private final val storeRepository: StoreRepository
    private final val productMapperObjectFactory: ObjectFactory<ProductMapper>

    constructor(storeRepository: StoreRepository, productMapperObjectFactory: ObjectFactory<ProductMapper>) {
        this.storeRepository = storeRepository
        this.productMapperObjectFactory = productMapperObjectFactory
    }

    private fun getProductMapper(): ProductMapper {
        return this.productMapperObjectFactory.`object`
    }

    override fun toEntity(request: StoreRequest): Store {
        val storeId = this.handleData(request.id, UUID.randomUUID()) as UUID
        val handleStore = Store()
        return this.storeRepository.findById(storeId)
            .map { store ->
                handleStore.apply {
                    this.id = store.id
                    this.address = request.address
                }
            }
            .orElseGet {
                handleStore.apply {
                    this.address = request.address
                }
            }
    }

    override fun toShortResponse(entity: Store): StoreShortResponse {
        return StoreShortResponse(
            id = entity.id,
            address = entity.address
        )
    }

    override fun toFullResponse(entity: Store): StoreFullResponse {
        return StoreFullResponse(
            id = entity.id,
            address = entity.address,
            products = entity.storeProducts
                .map { storeProduct ->
                    ProductAmountResponse(
                        this.getProductMapper().toShortResponse(storeProduct.product as Product),
                        storeProduct.amount
                    )
                }
                .toList()
        )
    }
}