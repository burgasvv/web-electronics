package org.burgas.webelectronics.entity.pk

import jakarta.persistence.*
import org.burgas.webelectronics.entity.BaseEntity
import org.burgas.webelectronics.entity.product.Product
import org.burgas.webelectronics.entity.store.Store

@Entity
@IdClass(StoreProductPK::class)
@Table(name = "store_product", schema = "public")
class StoreProduct : BaseEntity {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id", referencedColumnName = "id")
    var store: Store? = null

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    var product: Product? = null

    @Column(name = "amount", nullable = false)
    var amount: Long = 0

    constructor()

    @Suppress("unused")
    constructor(store: Store?, product: Product?, amount: Long) {
        this.store = store
        this.product = product
        this.amount = amount
    }
}