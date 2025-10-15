package org.burgas.webelectronics.mapper

import org.burgas.webelectronics.dto.pk.BucketProductFullRequest
import org.burgas.webelectronics.dto.pk.BucketProductShortRequest
import org.burgas.webelectronics.entity.pk.BucketProduct
import org.burgas.webelectronics.exception.BucketProductNotFoundException
import org.burgas.webelectronics.message.BucketProductMessages
import org.burgas.webelectronics.repository.BucketProductRepository
import org.burgas.webelectronics.service.BucketService
import org.burgas.webelectronics.service.ProductService
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Component

@Component
class BucketProductMapper {

    private final val bucketProductRepository: BucketProductRepository
    private final val bucketServiceObjectFactory: ObjectFactory<BucketService>
    private final val productServiceObjectFactory: ObjectFactory<ProductService>

    constructor(
        bucketProductRepository: BucketProductRepository,
        bucketServiceObjectFactory: ObjectFactory<BucketService>,
        productServiceObjectFactory: ObjectFactory<ProductService>
    ) {
        this.bucketProductRepository = bucketProductRepository
        this.bucketServiceObjectFactory = bucketServiceObjectFactory
        this.productServiceObjectFactory = productServiceObjectFactory
    }

    private fun getBucketService(): BucketService {
        return bucketServiceObjectFactory.getObject()
    }

    private fun getProductService(): ProductService {
        return productServiceObjectFactory.getObject()
    }

    fun toFullEntity(bucketProductFullRequest: BucketProductFullRequest): BucketProduct {
        val bucket = this.getBucketService().findEntity(bucketProductFullRequest.bucketId)
        val product = this.getProductService().findEntity(bucketProductFullRequest.productId)
        return this.bucketProductRepository.findBucketProductByBucketAndProduct(bucket, product)
            .map {
                BucketProduct().apply {
                    this.bucket = it.bucket
                    this.product = it.product
                    this.amount = bucketProductFullRequest.amount
                    this.price = bucketProductFullRequest.price
                }
            }
            .orElseGet {
                BucketProduct().apply {
                    this.bucket = bucket
                    this.product = product
                    this.amount = bucketProductFullRequest.amount
                    this.price = bucketProductFullRequest.price
                }
            }
    }

    fun toShortEntity(bucketProductShortRequest: BucketProductShortRequest): BucketProduct {
        val bucket = this.getBucketService().findEntity(bucketProductShortRequest.bucketId)
        val product = this.getProductService().findEntity(bucketProductShortRequest.productId)
        return this.bucketProductRepository.findBucketProductByBucketAndProduct(bucket, product)
            .orElseThrow { throw BucketProductNotFoundException(BucketProductMessages.BUCKET_PRODUCT_NOT_FOUND.message) }
    }
}