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
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class CategoryService : CrudService<CategoryRequest, Category, CategoryShortResponse, CategoryFullResponse> {

    private final val logger = LoggerFactory.getLogger(CategoryService::class.java)

    private final val categoryRepository: CategoryRepository
    private final val categoryMapper: CategoryMapper
    private final val imageServiceObjectFactory: ObjectFactory<ImageService>
    private final val redisTemplate: RedisTemplate<String, Category>

    constructor(
        categoryRepository: CategoryRepository,
        categoryMapper: CategoryMapper,
        imageServiceObjectFactory: ObjectFactory<ImageService>,
        redisTemplate: RedisTemplate<String, Category>
    ) {
        this.categoryRepository = categoryRepository
        this.categoryMapper = categoryMapper
        this.imageServiceObjectFactory = imageServiceObjectFactory
        this.redisTemplate = redisTemplate
    }

    companion object {
        private const val CACHE_KEY_PREFIX = "Category id: %s"
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
        val cacheKey = CACHE_KEY_PREFIX.format(id)
        val category = this.redisTemplate.opsForValue().get(cacheKey)

        if (category != null) {
            this.logger.info("Category from cache: $cacheKey")
            return this.categoryMapper.toFullResponse(category)
        }

        this.logger.info("Category not found in cache: $cacheKey")
        val findEntity = this.findEntity(id)

        this.redisTemplate.opsForValue().set(cacheKey, findEntity)
        this.logger.info("Category was set in cache: $cacheKey")

        return this.categoryMapper.toFullResponse(findEntity)
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    override fun createOrUpdate(request: CategoryRequest): CategoryFullResponse {
        if (request.id != null) {
            val cacheKey = CACHE_KEY_PREFIX.format(request.id)
            this.redisTemplate.delete(cacheKey)
            this.logger.info("Cache invalidated for updated product: $cacheKey")
        }
        return this.categoryMapper.toFullResponse(
            this.categoryRepository.save(this.categoryMapper.toEntity(request))
        )
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    override fun delete(id: UUID) {
        val cacheKey = CACHE_KEY_PREFIX.format(id)
        val category = this.findEntity(id)
        this.categoryRepository.delete(category)
        this.redisTemplate.delete(cacheKey)
        this.logger.info("Cache invalidated for deleted category: $cacheKey")
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun createImage(categoryId: UUID, part: Part) {
        val category = this.findEntity(categoryId)
        val cacheKey = CACHE_KEY_PREFIX.format(category.id)
        this.redisTemplate.delete(cacheKey)
        this.logger.info("Cache invalidated for category with image for create: $cacheKey")
        val image = this.getImageService().createImage(part)
        category.apply {
            this.image = image
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun changeImage(categoryId: UUID, part: Part) {
        val category = this.findEntity(categoryId)
        val cacheKey = CACHE_KEY_PREFIX.format(category.id)
        this.redisTemplate.delete(cacheKey)
        this.logger.info("Cache invalidated for category with image for change: $cacheKey")
        val image = category.image ?: throw ImageNotFoundException(ImageMessages.IMAGE_NOT_FOUND.message)
        this.getImageService().changeImage(image.id, part)
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun deleteImage(categoryId: UUID) {
        val category = this.findEntity(categoryId)
        val cacheKey = CACHE_KEY_PREFIX.format(category.id)
        this.redisTemplate.delete(cacheKey)
        this.logger.info("Cache invalidated for category with image for delete: $cacheKey")
        val image = category.image ?: throw ImageNotFoundException(ImageMessages.IMAGE_NOT_FOUND.message)
        category.apply {
            this.image = null
        }
        this.getImageService().deleteImage(image.id)
    }
}