package org.burgas.webelectronics.entity.image

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "image", schema = "public")
class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    lateinit var id: UUID

    @Column(name = "name", nullable = false)
    lateinit var name: String

    @Column(name = "content_type", nullable = false)
    lateinit var contentType: String

    @Column(name = "format", nullable = false)
    lateinit var format: String

    @Column(name = "size", nullable = false)
    var size: Long = 0

    @JsonIgnore
    @Column(name = "data", nullable = false)
    var data: ByteArray = byteArrayOf()

    constructor()

    @Suppress("unused")
    constructor(id: UUID, name: String, contentType: String, format: String, size: Long, data: ByteArray) {
        this.id = id
        this.name = name
        this.contentType = contentType
        this.format = format
        this.size = size
        this.data = data
    }
}