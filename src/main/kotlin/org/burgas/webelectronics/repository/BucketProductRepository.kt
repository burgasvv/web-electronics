package org.burgas.webelectronics.repository

import jakarta.persistence.LockModeType
import org.burgas.webelectronics.entity.bucket.Bucket
import org.burgas.webelectronics.entity.pk.BucketProduct
import org.burgas.webelectronics.entity.pk.BucketProductPK
import org.burgas.webelectronics.entity.product.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface BucketProductRepository : JpaRepository<BucketProduct, BucketProductPK> {

    @Lock(LockModeType.WRITE)
    fun findBucketProductByBucketAndProduct(bucket: Bucket, product: Product): Optional<BucketProduct>
}