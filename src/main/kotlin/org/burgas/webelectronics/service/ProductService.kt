package org.burgas.webelectronics.service

import jakarta.servlet.http.Part
import org.burgas.webelectronics.dto.product.ProductFullResponse
import org.burgas.webelectronics.dto.product.ProductRequest
import org.burgas.webelectronics.dto.product.ProductShortResponse
import org.burgas.webelectronics.entity.product.Product
import org.burgas.webelectronics.exception.ImageNotFoundException
import org.burgas.webelectronics.exception.ProductNotFoundException
import org.burgas.webelectronics.mapper.ProductMapper
import org.burgas.webelectronics.message.ImageMessages
import org.burgas.webelectronics.message.ProductMessages.PRODUCT_NOT_FOUND
import org.burgas.webelectronics.repository.ProductRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class ProductService : CrudService<ProductRequest, Product, ProductShortResponse, ProductFullResponse> {

    private final val productRepository: ProductRepository
    private final val productMapper: ProductMapper
    private final val imageServiceObjectFactory: ObjectFactory<ImageService>

    constructor(
        productRepository: ProductRepository,
        productMapper: ProductMapper,
        imageServiceObjectFactory1: ObjectFactory<ImageService>
    ) {
        this.productRepository = productRepository
        this.productMapper = productMapper
        this.imageServiceObjectFactory = imageServiceObjectFactory1
    }

    private fun getImageService(): ImageService {
        return imageServiceObjectFactory.`object`
    }

    override fun findEntity(id: UUID): Product {
        return this.productRepository.findById(id)
            .orElseThrow { throw ProductNotFoundException(PRODUCT_NOT_FOUND.message) }
    }

    override fun findAll(): List<ProductShortResponse> {
        return this.productRepository.findAll()
            .map { product -> this.productMapper.toShortResponse(product) }
            .toList()
    }

    override fun findById(id: UUID): ProductFullResponse {
        return this.productMapper.toFullResponse(this.findEntity(id))
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    override fun createOrUpdate(request: ProductRequest): ProductFullResponse {
        return this.productMapper.toFullResponse(
            this.productRepository.save(this.productMapper.toEntity(request))
        )
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    override fun delete(id: UUID) {
        val product = this.findEntity(id)
        this.productRepository.delete(product)
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun createImage(productId: UUID, part: Part) {
        val product = this.findEntity(productId)
        val image = this.getImageService().createImage(part)
        product.apply {
            this.image = image
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun changeImage(productId: UUID, part: Part) {
        val product = this.findEntity(productId)
        val image = product.image ?: throw ImageNotFoundException(ImageMessages.IMAGE_NOT_FOUND.message)
        this.getImageService().changeImage(image.id, part)
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun deleteImage(productId: UUID) {
        val product = this.findEntity(productId)
        val image = product.image ?: throw ImageNotFoundException(ImageMessages.IMAGE_NOT_FOUND.message)
        product.apply {
            this.image = null
        }
        this.getImageService().deleteImage(image.id)
    }
}