package org.burgas.webelectronics.service

import org.burgas.webelectronics.dto.bucket.BucketFullResponse
import org.burgas.webelectronics.dto.pk.BucketProductFullRequest
import org.burgas.webelectronics.dto.pk.BucketProductShortRequest
import org.burgas.webelectronics.entity.bucket.Bucket
import org.burgas.webelectronics.exception.BucketNotFoundException
import org.burgas.webelectronics.exception.NotEnoughProductAmountException
import org.burgas.webelectronics.exception.ProductAmountNotFoundException
import org.burgas.webelectronics.mapper.BucketMapper
import org.burgas.webelectronics.message.BucketMessages
import org.burgas.webelectronics.message.ProductMessages.PRODUCT_AMOUNT_NOT_ENOUGH
import org.burgas.webelectronics.message.ProductMessages.PRODUCT_AMOUNT_NOT_FOUND
import org.burgas.webelectronics.repository.BucketRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class BucketService {

    private final val bucketRepository: BucketRepository
    private final val bucketMapper: BucketMapper
    private final val storeProductServiceObjectFactory: ObjectFactory<StoreProductService>
    private final val productServiceObjectFactory: ObjectFactory<ProductService>
    private final val bucketProductServiceObjectFactory: ObjectFactory<BucketProductService>

    constructor(
        bucketRepository: BucketRepository,
        bucketMapper: BucketMapper,
        storeProductServiceObjectFactory: ObjectFactory<StoreProductService>,
        productServiceObjectFactory: ObjectFactory<ProductService>,
        bucketProductServiceObjectFactory: ObjectFactory<BucketProductService>
    ) {
        this.bucketRepository = bucketRepository
        this.bucketMapper = bucketMapper
        this.storeProductServiceObjectFactory = storeProductServiceObjectFactory
        this.productServiceObjectFactory = productServiceObjectFactory
        this.bucketProductServiceObjectFactory = bucketProductServiceObjectFactory
    }

    private fun getStoreProductService(): StoreProductService {
        return this.storeProductServiceObjectFactory.`object`
    }

    private fun getProductService(): ProductService {
        return this.productServiceObjectFactory.`object`
    }

    private fun getBucketProductService(): BucketProductService {
        return this.bucketProductServiceObjectFactory.`object`
    }

    fun findEntity(bucketId: UUID): Bucket {
        return this.bucketRepository.findById(bucketId)
            .orElseThrow { throw BucketNotFoundException(BucketMessages.BUCKET_NOT_FOUND.message) }
    }

    fun findById(bucketId: UUID): BucketFullResponse {
        return this.bucketMapper.toFullResponse(this.findEntity(bucketId))
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun addProduct(bucketProductShortRequest: BucketProductShortRequest): UUID {
        val amount = bucketProductShortRequest.amount ?: throw ProductAmountNotFoundException(PRODUCT_AMOUNT_NOT_FOUND.message)
        val productAmount = this.getStoreProductService().findByProductId(bucketProductShortRequest.productId)
            .sumOf { storeProduct -> storeProduct.amount }
        if (productAmount > amount) {
            val product = this.getProductService().findEntity(bucketProductShortRequest.productId)
            val bucketProductFullRequest = BucketProductFullRequest(
                bucketId = bucketProductShortRequest.bucketId,
                productId = bucketProductShortRequest.productId,
                amount = amount,
                price = product.price * amount
            )
            this.getBucketProductService().addProduct(bucketProductFullRequest)
            val bucket = this.findEntity(bucketProductFullRequest.bucketId)
            bucket.apply {
                this.balance += bucketProductFullRequest.price
            }
            return bucket.id

        } else {
            throw NotEnoughProductAmountException(PRODUCT_AMOUNT_NOT_ENOUGH.message)
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun removeProduct(bucketProductShortRequest: BucketProductShortRequest): UUID {
        val bucket = this.findEntity(bucketProductShortRequest.bucketId)
        val bucketProduct = this.getBucketProductService().findById(bucketProductShortRequest)
        bucket.apply {
            this.balance -= bucketProduct.price
        }
        this.getBucketProductService().removeProduct(bucketProductShortRequest)
        return bucket.id
    }
}