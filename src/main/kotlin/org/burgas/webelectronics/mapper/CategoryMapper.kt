package org.burgas.webelectronics.mapper

import org.burgas.webelectronics.dto.category.CategoryFullResponse
import org.burgas.webelectronics.dto.category.CategoryRequest
import org.burgas.webelectronics.dto.category.CategoryShortResponse
import org.burgas.webelectronics.entity.category.Category
import org.burgas.webelectronics.message.CategoryMessages.DESCRIPTION_FIELD_EMPTY
import org.burgas.webelectronics.message.CategoryMessages.NAME_FIELD_EMPTY
import org.burgas.webelectronics.repository.CategoryRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class CategoryMapper : EntityMapper<CategoryRequest, Category, CategoryShortResponse, CategoryFullResponse> {

    private final val categoryRepository: CategoryRepository
    private final val productMapperObjectFactory: ObjectFactory<ProductMapper>

    constructor(categoryRepository: CategoryRepository, productMapperObjectFactory: ObjectFactory<ProductMapper>) {
        this.categoryRepository = categoryRepository
        this.productMapperObjectFactory = productMapperObjectFactory
    }

    private fun getProductMapper(): ProductMapper {
        return this.productMapperObjectFactory.`object`
    }

    override fun toEntity(request: CategoryRequest): Category {
        val categoryId = this.handleData(request.id, UUID.randomUUID()) as UUID
        return this.categoryRepository.findById(categoryId)
            .map { category ->
                val name = this.handleData(request.name, category.name) as String
                val description = this.handleData(request.description, category.description) as String
                Category().apply {
                    this.id = category.id
                    this.name = name
                    this.description = description
                }
            }
            .orElseGet {
                val name = this.handleDataThrowable(request.name, NAME_FIELD_EMPTY.message) as String
                val description = this.handleDataThrowable(request.description,
                    DESCRIPTION_FIELD_EMPTY.message) as String
                Category().apply {
                    this.name = name
                    this.description = description
                }
            }
    }

    override fun toShortResponse(entity: Category): CategoryShortResponse {
        return CategoryShortResponse(
            id = entity.id,
            name = entity.name,
            description = entity.description
        )
    }

    override fun toFullResponse(entity: Category): CategoryFullResponse {
        return CategoryFullResponse(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            products = entity.products
                .map { product -> this.getProductMapper().toShortResponse(product) }
                .toList()
        )
    }
}