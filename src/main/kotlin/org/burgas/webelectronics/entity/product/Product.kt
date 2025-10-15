package org.burgas.webelectronics.entity.product

import jakarta.persistence.*
import org.burgas.webelectronics.entity.BaseEntity
import org.burgas.webelectronics.entity.category.Category
import org.burgas.webelectronics.entity.image.Image
import org.burgas.webelectronics.entity.pk.StoreProduct
import java.util.*

@Entity
@Table(name = "product", schema = "public")
@NamedEntityGraph(
    name = "product-entity-graph",
    attributeNodes = [
        NamedAttributeNode(value = "category", subgraph = "category-image-subgraph"),
        NamedAttributeNode(value = "image"),
        NamedAttributeNode(value = "storeProducts", subgraph = "store-subgraph")
    ],
    subgraphs = [
        NamedSubgraph(
            name = "category-image-subgraph",
            attributeNodes = [
                NamedAttributeNode(value = "image")
            ]
        ),
        NamedSubgraph(
            name = "store-subgraph",
            attributeNodes = [
                NamedAttributeNode(value = "store", subgraph = "address-subgraph")
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    var image: Image? = null

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    var storeProducts: MutableList<StoreProduct> = mutableListOf()

    constructor()

    constructor(
        id: UUID,
        category: Category?,
        name: String,
        description: String,
        price: Double,
        image: Image?,
        storeProducts: MutableList<StoreProduct>
    ) {
        this.id = id
        this.category = category
        this.name = name
        this.description = description
        this.price = price
        this.image = image
        this.storeProducts = storeProducts
    }
}

