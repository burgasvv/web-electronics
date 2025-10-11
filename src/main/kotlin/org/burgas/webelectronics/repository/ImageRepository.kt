package org.burgas.webelectronics.repository

import org.burgas.webelectronics.entity.image.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ImageRepository : JpaRepository<Image, UUID> {
}