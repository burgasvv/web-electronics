package org.burgas.webelectronics.mapper

import org.burgas.webelectronics.dto.bucket.BucketShortResponse
import org.burgas.webelectronics.dto.bucket.BucketFullResponse
import org.burgas.webelectronics.dto.product.ProductAmountPriceResponse
import org.burgas.webelectronics.entity.bucket.Bucket
import org.burgas.webelectronics.entity.product.Product
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class BucketMapper {

    final val identityMapperObjectFactory: ObjectFactory<IdentityMapper>
    final val productMapperObjectFactory: ObjectFactory<ProductMapper>

    constructor(
        identityMapperObjectFactory: ObjectFactory<IdentityMapper>,
        productMapperObjectFactory: ObjectFactory<ProductMapper>
    ) {
        this.identityMapperObjectFactory = identityMapperObjectFactory
        this.productMapperObjectFactory = productMapperObjectFactory
    }

    private fun getIdentityMapper(): IdentityMapper {
        return this.identityMapperObjectFactory.`object`
    }

    private fun getProductMapper(): ProductMapper {
        return this.productMapperObjectFactory.`object`
    }

    fun toShortResponse(bucketEntity: Bucket): BucketShortResponse {
        return BucketShortResponse(
            id = bucketEntity.id,
            balance = bucketEntity.balance
        )
    }

    fun toFullResponse(bucketEntity: Bucket): BucketFullResponse {
        return BucketFullResponse(
            id = bucketEntity.id,
            balance = bucketEntity.balance,
            identity = Optional.ofNullable(bucketEntity.identity)
                .map { identity -> this.getIdentityMapper().toShortResponse(identity) }
                .orElse(null),
            products = bucketEntity.bucketProducts
                .map { bucketProduct ->
                    ProductAmountPriceResponse(
                        product = this.getProductMapper().toShortResponse(bucketProduct.product as Product),
                        amount = bucketProduct.amount,
                        price = bucketProduct.price
                    )
                }
                .toList()
        )
    }
}