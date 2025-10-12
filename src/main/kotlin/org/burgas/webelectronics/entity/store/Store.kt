package org.burgas.webelectronics.entity.store

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.NamedAttributeNode
import jakarta.persistence.NamedEntityGraph
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.burgas.webelectronics.entity.address.Address
import org.burgas.webelectronics.entity.pk.StoreProduct
import java.util.UUID

@Entity
@Table(name = "store", schema = "public")
@NamedEntityGraph(
    name = "store-entity-graph",
    attributeNodes = [
        NamedAttributeNode(value = "address"),
        NamedAttributeNode(value = "storeProducts")
    ]
)
class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    lateinit var id: UUID

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    var address: Address? = null

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var storeProducts: MutableList<StoreProduct> = mutableListOf()

    constructor()

    @Suppress("unused")
    constructor(id: UUID, address: Address?, storeProducts: MutableList<StoreProduct>) {
        this.id = id
        this.address = address
        this.storeProducts = storeProducts
    }
}