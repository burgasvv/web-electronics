package org.burgas.webelectronics.entity.category

import jakarta.persistence.*
import org.burgas.webelectronics.entity.BaseEntity
import org.burgas.webelectronics.entity.image.Image
import org.burgas.webelectronics.entity.product.Product
import java.util.*

@Entity
@Table(name = "category", schema = "public")
@NamedEntityGraph(
    name = "category-entity-graph",
    attributeNodes = [
        NamedAttributeNode("image"),
        NamedAttributeNode(value = "products", subgraph = "products-category-image-subgraph")
    ],
    subgraphs = [
        NamedSubgraph(
            name = "products-category-image-subgraph",
            attributeNodes = [
                NamedAttributeNode("image"),
                NamedAttributeNode("category"),
            ]
        )
    ]
)
class Category : BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: UUID

    @Column(name = "name", nullable = false, unique = true)
    lateinit var name: String

    @Column(name = "description", nullable = false, unique = true)
    lateinit var description: String

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    var image: Image? = null

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    var products: MutableList<Product> = mutableListOf()

    constructor()

    @Suppress("unused")
    constructor(id: UUID, name: String, description: String, products: MutableList<Product>) {
        this.id = id
        this.name = name
        this.description = description
        this.products = products
    }
}