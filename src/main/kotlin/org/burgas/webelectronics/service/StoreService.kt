package org.burgas.webelectronics.service

import org.burgas.webelectronics.entity.store.Store
import org.burgas.webelectronics.exception.StoreNotFoundException
import org.burgas.webelectronics.message.StoreMessages
import org.burgas.webelectronics.repository.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
class StoreService {

    final val storeRepository: StoreRepository

    constructor(storeRepository: StoreRepository) {
        this.storeRepository = storeRepository
    }

    fun findEntity(storeId: UUID): Store {
        return this.storeRepository.findById(storeId)
            .orElseThrow { throw StoreNotFoundException(StoreMessages.STORE_NOT_FOUND.message) }
    }

    fun findAll(): List<Store> {
        return this.storeRepository.findAll()
    }

    fun findById(storeId: UUID): Store {
        return this.findEntity(storeId)
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun createOrUpdate(store: Store): Store {
        return this.storeRepository.save(store)
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    fun delete(storeId: UUID) {
        val store = this.findEntity(storeId)
        this.storeRepository.delete(store)
    }
}