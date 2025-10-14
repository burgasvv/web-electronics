package org.burgas.webelectronics.service

import jakarta.servlet.http.Part
import org.burgas.webelectronics.dto.category.CategoryFullResponse
import org.burgas.webelectronics.dto.category.CategoryRequest
import org.burgas.webelectronics.dto.category.CategoryShortResponse
import org.burgas.webelectronics.entity.category.Category
import org.burgas.webelectronics.exception.CategoryNotFoundException
import org.burgas.webelectronics.exception.ImageNotFoundException
import org.burgas.webelectronics.mapper.CategoryMapper
import org.burgas.webelectronics.message.CategoryMessages
import org.burgas.webelectronics.message.ImageMessages
import org.burgas.webelectronics.repository.CategoryRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class CategoryService : CrudService<CategoryRequest, Category, CategoryShortResponse, CategoryFullResponse> {

    private final val categoryRepository: CategoryRepository
    private final val categoryMapper: CategoryMapper
    private final val imageServiceObjectFactory: ObjectFactory<ImageService>

    constructor(
        categoryRepository: CategoryRepository,
        categoryMapper: CategoryMapper,
        imageServiceObjectFactory1: ObjectFactory<ImageService>
    ) {
        this.categoryRepository = categoryRepository
        this.categoryMapper = categoryMapper
        this.imageServiceObjectFactory = imageServiceObjectFactory1
    }

    private fun getImageService(): ImageService {
        return this.imageServiceObjectFactory.`object`
    }

    override fun findEntity(id: UUID): Category {
        return this.categoryRepository.findById(id)
            .orElseThrow { throw CategoryNotFoundException(CategoryMessages.CATEGORY_NOT_FOUND.message) }
    }

    override fun findAll(): List<CategoryShortResponse> {
        return this.categoryRepository.findAll()
            .map { category -> this.categoryMapper.toShortResponse(category) }
            .toList()
    }

    override fun findById(id: UUID): CategoryFullResponse {
        return this.categoryMapper.toFullResponse(this.findEntity(id))
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    override fun createOrUpdate(request: CategoryRequest): CategoryFullResponse {
        return this.categoryMapper.toFullResponse(
            this.categoryRepository.save(this.categoryMapper.toEntity(request))
        )
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    override fun delete(id: UUID) {
        val category = this.findEntity(id)
        this.categoryRepository.delete(category)
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun createImage(categoryId: UUID, part: Part) {
        val category = this.findEntity(categoryId)
        val image = this.getImageService().createImage(part)
        category.apply {
            this.image = image
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun changeImage(categoryId: UUID, part: Part) {
        val category = this.findEntity(categoryId)
        val image = category.image ?: throw ImageNotFoundException(ImageMessages.IMAGE_NOT_FOUND.message)
        this.getImageService().changeImage(image.id, part)
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun deleteImage(categoryId: UUID) {
        val category = this.findEntity(categoryId)
        val image = category.image ?: throw ImageNotFoundException(ImageMessages.IMAGE_NOT_FOUND.message)
        category.apply {
            this.image = null
        }
        this.getImageService().deleteImage(image.id)
    }
}