package org.burgas.webelectronics.service

import org.burgas.webelectronics.dto.pk.StoreProductFullRequest
import org.burgas.webelectronics.dto.pk.StoreProductShortRequest
import org.burgas.webelectronics.mapper.StoreProductMapper
import org.burgas.webelectronics.repository.StoreProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class StoreProductService {

    private final val storeProductRepository: StoreProductRepository
    private final val storeProductMapper: StoreProductMapper

    constructor(storeProductRepository: StoreProductRepository, storeProductMapper: StoreProductMapper) {
        this.storeProductRepository = storeProductRepository
        this.storeProductMapper = storeProductMapper
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun add(storeProductFullRequest: StoreProductFullRequest) {
        val storeProduct = this.storeProductMapper.toFullEntity(storeProductFullRequest)
        this.storeProductRepository.save(storeProduct)
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun delete(storeProductShortRequest: StoreProductShortRequest) {
        val storeProduct = this.storeProductMapper.toShortEntity(storeProductShortRequest)
        this.storeProductRepository.delete(storeProduct)
    }
}