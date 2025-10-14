package org.burgas.webelectronics.mapper

import org.burgas.webelectronics.dto.product.ProductFullResponse
import org.burgas.webelectronics.dto.product.ProductRequest
import org.burgas.webelectronics.dto.product.ProductShortResponse
import org.burgas.webelectronics.entity.category.Category
import org.burgas.webelectronics.entity.product.Product
import org.burgas.webelectronics.entity.store.Store
import org.burgas.webelectronics.message.ProductMessages.*
import org.burgas.webelectronics.repository.CategoryRepository
import org.burgas.webelectronics.repository.ProductRepository
import org.springframework.beans.factory.ObjectFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class ProductMapper : EntityMapper<ProductRequest, Product, ProductShortResponse, ProductFullResponse> {

    private final val productRepository: ProductRepository
    private final val categoryMapperObjectFactory: ObjectFactory<CategoryMapper>
    private final val storeMapperObjectFactory: ObjectFactory<StoreMapper>
    private final val categoryRepositoryObjectMapper: ObjectFactory<CategoryRepository>

    constructor(
        productRepository: ProductRepository,
        categoryMapperObjectFactory: ObjectFactory<CategoryMapper>,
        storeMapperObjectFactory: ObjectFactory<StoreMapper>,
        categoryRepositoryObjectMapper: ObjectFactory<CategoryRepository>
    ) {
        this.productRepository = productRepository
        this.categoryMapperObjectFactory = categoryMapperObjectFactory
        this.storeMapperObjectFactory = storeMapperObjectFactory
        this.categoryRepositoryObjectMapper = categoryRepositoryObjectMapper
    }

    private fun getCategoryMapper(): CategoryMapper {
        return this.categoryMapperObjectFactory.`object`
    }

    private fun getStoreMapper(): StoreMapper {
        return this.storeMapperObjectFactory.`object`
    }

    private fun getCategoryRepository(): CategoryRepository {
        return this.categoryRepositoryObjectMapper.`object`
    }

    override fun toEntity(request: ProductRequest): Product {
        val productId = this.handleData(request.id, UUID.randomUUID()) as UUID
        return this.productRepository.findById(productId)
            .map { product ->
                val categoryId = this.handleData(request.categoryId, UUID.randomUUID()) as UUID
                val findCategory = this.getCategoryRepository().findById(categoryId).orElse(null)
                val category = this.handleData(findCategory, product.category) as Category
                val name = this.handleData(request.name, product.name) as String
                val description = this.handleData(request.description, product.description) as String
                val price = this.handleData(request.price, product.price) as Double
                Product().apply {
                    this.id = product.id
                    this.category = category
                    this.name = name
                    this.description = description
                    this.price = price
                }
            }
            .orElseGet {
                val categoryId = this.handleData(request.categoryId, UUID.randomUUID()) as UUID
                val findCategory = this.getCategoryRepository().findById(categoryId).orElse(null)
                val category = this.handleDataThrowable(findCategory, CATEGORY_FIELD_EMPTY.message)
                val name = this.handleDataThrowable(request.name, NAME_FIELD_EMPTY.message) as String
                val description =
                    this.handleDataThrowable(request.description, DESCRIPTION_FIELD_EMPTY.message) as String
                val price = this.handleDataThrowable(request.price, PRICE_FIELD_EMPTY.message) as Double
                Product().apply {
                    this.category = category
                    this.name = name
                    this.description = description
                    this.price = price
                }
            }
    }

    override fun toShortResponse(entity: Product): ProductShortResponse {
        return ProductShortResponse(
            id = entity.id,
            category = Optional.ofNullable(entity.category)
                .map { category -> this.getCategoryMapper().toShortResponse(category) }
                .orElse(null),
            name = entity.name,
            description = entity.description,
            price = entity.price
        )
    }

    override fun toFullResponse(entity: Product): ProductFullResponse {
        return ProductFullResponse(
            id = entity.id,
            category = Optional.ofNullable(entity.category)
                .map { category -> this.getCategoryMapper().toShortResponse(category) }
                .orElse(null),
            name = entity.name,
            description = entity.description,
            price = entity.price,
            stores = entity.storeProducts
                .map { storeProduct -> storeProduct.store as Store }
                .map { store -> this.getStoreMapper().toShortResponse(store) }
                .toList()
        )
    }
}