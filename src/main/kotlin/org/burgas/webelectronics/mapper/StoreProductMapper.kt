package org.burgas.webelectronics.mapper

import org.burgas.webelectronics.dto.pk.StoreProductFullRequest
import org.burgas.webelectronics.dto.pk.StoreProductShortRequest
import org.burgas.webelectronics.entity.pk.StoreProduct
import org.burgas.webelectronics.entity.pk.StoreProductPK
import org.burgas.webelectronics.exception.StoreProductNotFoundException
import org.burgas.webelectronics.message.StoreProductMessages
import org.burgas.webelectronics.repository.StoreProductRepository
import org.burgas.webelectronics.service.ProductService
import org.burgas.webelectronics.service.StoreService
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Component

@Component
class StoreProductMapper {

    private final val storeProductRepository: StoreProductRepository
    private final val storeServiceObjectFactory: ObjectFactory<StoreService>
    private final val productServiceObjectFactory: ObjectFactory<ProductService>

    constructor(
        storeProductRepository: StoreProductRepository,
        storeServiceObjectFactory: ObjectFactory<StoreService>,
        productServiceObjectFactory: ObjectFactory<ProductService>
    ) {
        this.storeProductRepository = storeProductRepository
        this.storeServiceObjectFactory = storeServiceObjectFactory
        this.productServiceObjectFactory = productServiceObjectFactory
    }

    private fun getStoreService(): StoreService {
        return storeServiceObjectFactory.`object`
    }

    private fun getProductService(): ProductService {
        return productServiceObjectFactory.`object`
    }

    fun toFullEntity(storeProductFullRequest: StoreProductFullRequest): StoreProduct {
        val store = this.getStoreService().findEntity(storeProductFullRequest.storeId)
        val product = this.getProductService().findEntity(storeProductFullRequest.productId)
        val storeProductPK = StoreProductPK(store, product)
        return this.storeProductRepository.findById(storeProductPK)
            .map { storeProduct ->
                StoreProduct().apply {
                    this.store = storeProduct.store
                    this.product = storeProduct.product
                    this.amount = storeProductFullRequest.amount
                }
            }
            .orElseGet {
                StoreProduct().apply {
                    this.store = store
                    this.product = product
                    this.amount = storeProductFullRequest.amount
                }
            }
    }

    fun toShortEntity(storeProductShortRequest: StoreProductShortRequest): StoreProduct {
        val store = this.getStoreService().findEntity(storeProductShortRequest.storeId)
        val product = this.getProductService().findEntity(storeProductShortRequest.productId)
        val storeProductPK = StoreProductPK(store, product)
        return this.storeProductRepository.findById(storeProductPK)
            .orElseThrow { throw StoreProductNotFoundException(StoreProductMessages.STORE_PRODUCT_NOT_FOUND.message) }
    }
}