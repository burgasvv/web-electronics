package org.burgas.webelectronics.entity.pk

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.NamedAttributeNode
import jakarta.persistence.NamedEntityGraph
import jakarta.persistence.Table
import org.burgas.webelectronics.entity.BaseEntity
import org.burgas.webelectronics.entity.product.Product
import org.burgas.webelectronics.entity.store.Store

@Entity
@IdClass(StoreProductPK::class)
@Table(name = "store_product", schema = "public")
@NamedEntityGraph(
    name = "store-product-entity-graph",
    attributeNodes = [
        NamedAttributeNode(value = "store"),
        NamedAttributeNode(value = "product"),
    ]
)
class StoreProduct : BaseEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", referencedColumnName = "id")
    var store: Store? = null

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
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