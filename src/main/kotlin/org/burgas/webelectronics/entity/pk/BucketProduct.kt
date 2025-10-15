package org.burgas.webelectronics.entity.pk

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.burgas.webelectronics.entity.BaseEntity
import org.burgas.webelectronics.entity.bucket.Bucket
import org.burgas.webelectronics.entity.product.Product

@Entity
@IdClass(BucketProductPK::class)
@Table(name = "bucket_product", schema = "public")
class BucketProduct : BaseEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucket_id", referencedColumnName = "id")
    var bucket: Bucket? = null

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    var product: Product? = null

    @Column(name = "amount", nullable = false)
    var amount: Long = 0

    @Column(name = "price", nullable = false)
    var price: Double = 0.0

    constructor()

    @Suppress("unused")
    constructor(bucket: Bucket?, product: Product?, amount: Long, price: Double) {
        this.bucket = bucket
        this.product = product
        this.amount = amount
        this.price = price
    }
}