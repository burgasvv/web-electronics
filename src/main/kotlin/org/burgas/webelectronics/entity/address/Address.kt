package org.burgas.webelectronics.entity.address

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.burgas.webelectronics.entity.BaseEntity
import java.util.UUID

@Entity
@Table(name = "address", schema = "public")
class Address : BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, unique = true)
    lateinit var id: UUID

    @Column(name = "city", nullable = false)
    lateinit var city: String

    @Column(name = "street", nullable = false)
    lateinit var street: String

    @Column(name = "house", nullable = false)
    lateinit var house: String

    @Column(name = "apartment", nullable = false)
    lateinit var apartment: String

    constructor()

    @Suppress("unused")
    constructor(id: UUID, city: String, street: String, house: String, apartment: String) {
        this.id = id
        this.city = city
        this.street = street
        this.house = house
        this.apartment = apartment
    }
}