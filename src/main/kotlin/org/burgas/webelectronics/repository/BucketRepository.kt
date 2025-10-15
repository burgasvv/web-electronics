package org.burgas.webelectronics.repository

import org.burgas.webelectronics.entity.bucket.Bucket
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface BucketRepository : JpaRepository<Bucket, UUID> {

    @EntityGraph(value = "bucket-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findAll(): List<Bucket>

    @EntityGraph(value = "bucket-entity-graph", type = EntityGraph.EntityGraphType.FETCH)
    override fun findById(id: UUID): Optional<Bucket>
}