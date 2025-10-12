package org.burgas.webelectronics.entity.pk

import jakarta.persistence.Embeddable
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne
import org.burgas.webelectronics.entity.product.Product
import org.burgas.webelectronics.entity.store.Store
import java.io.Serializable

@Embeddable
class StoreProductPK : Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    var store: Store? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var product: Product? = null

    constructor()

    @Suppress("unused")
    constructor(store: Store?, product: Product?) {
        this.store = store
        this.product = product
    }
}