package org.burgas.webelectronics.entity.product

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.NamedAttributeNode
import jakarta.persistence.NamedEntityGraph
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.burgas.webelectronics.entity.BaseEntity
import org.burgas.webelectronics.entity.category.Category
import org.burgas.webelectronics.entity.pk.StoreProduct
import java.util.UUID

@Entity
@Table(name = "product", schema = "public")
@NamedEntityGraph(
    name = "product-entity-graph",
    attributeNodes = [
        NamedAttributeNode(value = "category"),
        NamedAttributeNode(value = "storeProducts")
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

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
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

