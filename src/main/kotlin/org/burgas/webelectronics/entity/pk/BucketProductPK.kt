package org.burgas.webelectronics.entity.pk

import jakarta.persistence.Embeddable
import jakarta.persistence.ManyToOne
import org.burgas.webelectronics.entity.bucket.Bucket
import org.burgas.webelectronics.entity.product.Product
import java.io.Serializable

@Embeddable
class BucketProductPK : Serializable {

    @ManyToOne
    var bucket: Bucket? = null

    @ManyToOne
    var product: Product? = null

    constructor()

    @Suppress("unused")
    constructor(bucket: Bucket, product: Product) {
        this.bucket = bucket
        this.product = product
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BucketProductPK

        if (bucket != other.bucket) return false
        if (product != other.product) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bucket.hashCode()
        result = 31 * result + product.hashCode()
        return result
    }
}