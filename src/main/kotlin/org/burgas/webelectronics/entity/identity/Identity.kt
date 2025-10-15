package org.burgas.webelectronics.entity.identity

import jakarta.persistence.*
import org.burgas.webelectronics.entity.BaseEntity
import org.burgas.webelectronics.entity.bucket.Bucket
import org.burgas.webelectronics.entity.image.Image
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity
@Table(name = "identity", schema = "public")
@NamedEntityGraph(
    name = "identity-entity-graph",
    attributeNodes = [
        NamedAttributeNode(value = "image"),
        NamedAttributeNode(value = "bucket")
    ]
)
class Identity : BaseEntity, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    lateinit var id: UUID

    @Enumerated(EnumType.STRING)
    @Column(name = "authority", nullable = false)
    lateinit var authority: Authority

    @Column(name = "email", nullable = false, unique = true)
    lateinit var email: String

    @Column(name = "pass", nullable = false)
    lateinit var pass: String

    @Column(name = "firstname", nullable = false)
    lateinit var firstname: String

    @Column(name = "lastname", nullable = false)
    lateinit var lastname: String

    @Column(name = "patronymic", nullable = false)
    lateinit var patronymic: String

    @Column(name = "enabled", nullable = false)
    var enabled: Boolean = true

    @OneToOne(mappedBy = "identity", cascade = [CascadeType.ALL])
    var bucket: Bucket? = null

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    var image: Image? = null

    constructor()

    @Suppress("unused")
    constructor(
        id: UUID,
        authority: Authority,
        email: String,
        pass: String,
        firstname: String,
        lastname: String,
        patronymic: String,
        enabled: Boolean,
        bucket: Bucket?,
        image: Image?
    ) {
        this.id = id
        this.authority = authority
        this.email = email
        this.pass = pass
        this.firstname = firstname
        this.lastname = lastname
        this.patronymic = patronymic
        this.enabled = enabled
        this.bucket = bucket
        this.image = image
    }

    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return mutableListOf(this.authority)
    }

    override fun getPassword(): String? {
        return pass
    }

    override fun getUsername(): String? {
        return email
    }
}