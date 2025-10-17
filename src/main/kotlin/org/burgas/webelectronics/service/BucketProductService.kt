package org.burgas.webelectronics.service

import org.burgas.webelectronics.dto.pk.BucketProductFullRequest
import org.burgas.webelectronics.dto.pk.BucketProductShortRequest
import org.burgas.webelectronics.entity.bucket.Bucket
import org.burgas.webelectronics.entity.pk.BucketProduct
import org.burgas.webelectronics.entity.product.Product
import org.burgas.webelectronics.exception.BucketProductAmountException
import org.burgas.webelectronics.exception.ProductNotFoundException
import org.burgas.webelectronics.mapper.BucketProductMapper
import org.burgas.webelectronics.message.BucketProductMessages
import org.burgas.webelectronics.message.ProductMessages
import org.burgas.webelectronics.repository.BucketProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class BucketProductService {

    final val bucketProductRepository: BucketProductRepository
    final val bucketProductMapper: BucketProductMapper

    constructor(bucketProductRepository: BucketProductRepository, bucketProductMapper: BucketProductMapper) {
        this.bucketProductRepository = bucketProductRepository
        this.bucketProductMapper = bucketProductMapper
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun findById(bucketProductShortRequest: BucketProductShortRequest): BucketProduct {
        return this.bucketProductMapper.toShortEntity(bucketProductShortRequest)
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun addProduct(bucketProductFullRequest: BucketProductFullRequest): BucketProduct {
        val bucketProduct = this.bucketProductMapper.toFullEntity(bucketProductFullRequest)
        return this.bucketProductRepository.save(bucketProduct)
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun removeProduct(bucketProductShortRequest: BucketProductShortRequest) {
        val bucketProduct = this.bucketProductMapper.toShortEntity(bucketProductShortRequest)
        this.bucketProductRepository.delete(bucketProduct)
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun removeAllProducts(bucket: Bucket) {
        val bucketProducts = this.bucketProductRepository.findBucketProductsByBucket(bucket)
        this.bucketProductRepository.deleteAll(bucketProducts)
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun increaseProduct(bucketProductShortRequest: BucketProductShortRequest): Product {
        val bucketProduct = this.bucketProductMapper.toShortEntity(bucketProductShortRequest)
        val product = bucketProduct.product ?: throw ProductNotFoundException(ProductMessages.PRODUCT_NOT_FOUND.message)
        bucketProduct.apply {
            this.amount += 1
            this.price += product.price
        }
        return product
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun decreaseProduct(bucketProductShortRequest: BucketProductShortRequest): Product {
        val bucketProduct = this.bucketProductMapper.toShortEntity(bucketProductShortRequest)
        val product = bucketProduct.product ?: throw ProductNotFoundException(ProductMessages.PRODUCT_NOT_FOUND.message)
        if (bucketProduct.amount > 1) {
            bucketProduct.apply {
                this.amount -= 1
                this.price -= product.price
            }
            return product

        } else {
            throw BucketProductAmountException(BucketProductMessages.BUCKET_PRODUCT_WRONG_AMOUNT.message)
        }
    }
}