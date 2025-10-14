package org.burgas.webelectronics.entity.store

import jakarta.persistence.*
import org.burgas.webelectronics.entity.BaseEntity
import org.burgas.webelectronics.entity.address.Address
import org.burgas.webelectronics.entity.pk.StoreProduct
import java.util.*

@Entity
@Table(name = "store", schema = "public")
@NamedEntityGraph(
    name = "store-entity-graph",
    attributeNodes = [
        NamedAttributeNode(value = "address"),
        NamedAttributeNode(value = "storeProducts", subgraph = "product-subgraph")
    ],
    subgraphs = [
        NamedSubgraph(
            name = "product-subgraph",
            attributeNodes = [
                NamedAttributeNode(value = "product", subgraph = "category-subgraph")
            ]
        ),
        NamedSubgraph(
            name = "category-subgraph",
            attributeNodes = [
                NamedAttributeNode(value = "category")
            ]
        )
    ]
)
class Store : BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    lateinit var id: UUID

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    var address: Address? = null

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    var storeProducts: MutableList<StoreProduct> = mutableListOf()

    constructor()

    @Suppress("unused")
    constructor(id: UUID, address: Address?, storeProducts: MutableList<StoreProduct>) {
        this.id = id
        this.address = address
        this.storeProducts = storeProducts
    }
}