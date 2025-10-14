package org.burgas.webelectronics.entity.product

import jakarta.persistence.*
import org.burgas.webelectronics.entity.BaseEntity
import org.burgas.webelectronics.entity.category.Category
import org.burgas.webelectronics.entity.pk.StoreProduct
import java.util.*

@Entity
@Table(name = "product", schema = "public")
@NamedEntityGraph(
    name = "product-entity-graph",
    attributeNodes = [
        NamedAttributeNode(value = "category"),
        NamedAttributeNode(value = "storeProducts", subgraph = "store-subgraph")
    ],
    subgraphs = [
        NamedSubgraph(
            name = "store-subgraph",
            attributeNodes = [
                NamedAttributeNode(value = "store", subgraph = "address-subgraph"),
            ]
        ),
        NamedSubgraph(
            name = "address-subgraph",
            attributeNodes = [
                NamedAttributeNode(value = "address")
            ]
        )
    ]
)
class Product : BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID

    @ManyToOne(fetch = FetchType.LAZY)
    var category: Category? = null

    @Column(name = "name", nullable = false, unique = true)
    lateinit var name: String

    @Column(name = "description", nullable = false, unique = true)
    lateinit var description: String

    @Column(name = "price", nullable = false)
    var price: Double = 0.0

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    var storeProducts: MutableList<StoreProduct> = mutableListOf()

    constructor()

    @Suppress("unused")
    constructor(
        id: UUID,
        category: Category?,
        name: String,
        description: String,
        price: Double,
        storeProducts: MutableList<StoreProduct>
    ) {
        this.id = id
        this.category = category
        this.name = name
        this.description = description
        this.price = price
        this.storeProducts = storeProducts
    }
}

