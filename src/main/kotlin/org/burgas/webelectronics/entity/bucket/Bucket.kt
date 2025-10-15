package org.burgas.webelectronics.entity.bucket

import jakarta.persistence.*
import org.burgas.webelectronics.entity.BaseEntity
import org.burgas.webelectronics.entity.identity.Identity
import org.burgas.webelectronics.entity.pk.BucketProduct
import java.util.*

@Entity
@Table(name = "bucket", schema = "public")
@NamedEntityGraph(
    name = "bucket-entity-graph",
    attributeNodes = [
        NamedAttributeNode(value = "identity", subgraph = "identity-image-subgraph"),
        NamedAttributeNode(value = "bucketProducts", subgraph = "bucket-product-subgraph")
    ],
    subgraphs = [
        NamedSubgraph(
            name = "identity-image-subgraph",
            attributeNodes = [
                NamedAttributeNode(value = "image")
            ]
        ),
        NamedSubgraph(
            name = "bucket-product-subgraph",
            attributeNodes = [
                NamedAttributeNode(value = "product", subgraph = "product-category-image-subgraph")
            ]
        ),
        NamedSubgraph(
            name = "product-category-image-subgraph",
            attributeNodes = [
                NamedAttributeNode(value = "category", subgraph = "category-image-subgraph")
            ]
        ),
        NamedSubgraph(
            name = "product-category-image-subgraph",
            attributeNodes = [
                NamedAttributeNode(value = "image")
            ]
        ),
        NamedSubgraph(
            name = "category-image-subgraph",
            attributeNodes = [
                NamedAttributeNode(value = "image")
            ]
        )
    ]
)
class Bucket : BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    lateinit var id: UUID

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identity_id", referencedColumnName = "id")
    var identity: Identity? = null

    @Column(name = "balance", nullable = false)
    var balance: Double = 0.0

    @OneToMany(mappedBy = "bucket", fetch = FetchType.LAZY)
    var bucketProducts: MutableList<BucketProduct> = mutableListOf()

    constructor()

    @Suppress("unused")
    constructor(id: UUID, identity: Identity?, balance: Double, bucketProducts: MutableList<BucketProduct>) {
        this.id = id
        this.identity = identity
        this.balance = balance
        this.bucketProducts = bucketProducts
    }
}