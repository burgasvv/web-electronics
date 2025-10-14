package org.burgas.webelectronics.entity.pk

import jakarta.persistence.Embeddable
import jakarta.persistence.ManyToOne
import org.burgas.webelectronics.entity.product.Product
import org.burgas.webelectronics.entity.store.Store
import java.io.Serializable

@Embeddable
class StoreProductPK : Serializable {

    @ManyToOne
    var store: Store? = null

    @ManyToOne
    var product: Product? = null

    constructor()

    @Suppress("unused")
    constructor(store: Store?, product: Product?) {
        this.store = store
        this.product = product
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StoreProductPK

        if (store != other.store) return false
        if (product != other.product) return false

        return true
    }

    override fun hashCode(): Int {
        var result = store?.hashCode() ?: 0
        result = 31 * result + (product?.hashCode() ?: 0)
        return result
    }
}