package org.burgas.webelectronics.service

import org.burgas.webelectronics.dto.store.StoreFullResponse
import org.burgas.webelectronics.dto.store.StoreRequest
import org.burgas.webelectronics.dto.store.StoreShortResponse
import org.burgas.webelectronics.entity.store.Store
import org.burgas.webelectronics.exception.StoreNotFoundException
import org.burgas.webelectronics.mapper.StoreMapper
import org.burgas.webelectronics.message.StoreMessages
import org.burgas.webelectronics.repository.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class StoreService : CrudService<StoreRequest, Store, StoreShortResponse, StoreFullResponse> {

    private final val storeRepository: StoreRepository
    private final val storeMapper: StoreMapper

    constructor(storeRepository: StoreRepository, storeMapper: StoreMapper) {
        this.storeRepository = storeRepository
        this.storeMapper = storeMapper
    }

    override fun findEntity(id: UUID): Store {
        return this.storeRepository.findById(id)
            .orElseThrow { throw StoreNotFoundException(StoreMessages.STORE_NOT_FOUND.message) }
    }

    override fun findAll(): List<StoreShortResponse> {
        return this.storeRepository.findAll()
            .map { store -> this.storeMapper.toShortResponse(store) }
            .toList()
    }

    override fun findById(id: UUID): StoreFullResponse {
        return this.storeMapper.toFullResponse(this.findEntity(id))
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    override fun createOrUpdate(request: StoreRequest): StoreFullResponse {
        return this.storeMapper.toFullResponse(
            storeRepository.save(this.storeMapper.toEntity(request))
        )
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    override fun delete(id: UUID) {
        val store = this.findEntity(id)
        this.storeRepository.delete(store)
    }
}