package org.burgas.webelectronics.service

import jakarta.servlet.http.Part
import org.burgas.webelectronics.entity.image.Image
import org.burgas.webelectronics.exception.ImageNotFoundException
import org.burgas.webelectronics.message.ImageMessages
import org.burgas.webelectronics.repository.ImageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class ImageService {

    private final val imageRepository: ImageRepository

    constructor(imageRepository: ImageRepository) {
        this.imageRepository = imageRepository
    }

    fun findEntity(imageId: UUID): Image {
        return this.imageRepository.findById(imageId)
            .orElseThrow { throw ImageNotFoundException(ImageMessages.IMAGE_NOT_FOUND.message) }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun createImage(part: Part): Image {
        val image = Image().apply {
            this.name = part.name
            this.contentType = part.contentType
            this.format = part.contentType.split("/").last()
            this.size = part.size
            this.data = part.inputStream.readBytes()
        }
        return this.imageRepository.save(image)
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun changeImage(imageId: UUID, part: Part): Image {
        return this.findEntity(imageId).apply {
            this.name = part.name
            this.contentType = part.contentType
            this.format = part.contentType.split("/").last()
            this.size = part.size
            this.data = part.inputStream.readBytes()
        }
    }

    fun deleteImage(imageId: UUID) {
        val image = this.findEntity(imageId)
        this.imageRepository.delete(image)
    }
}