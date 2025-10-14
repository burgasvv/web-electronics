package org.burgas.webelectronics.repository

import org.burgas.webelectronics.entity.pk.StoreProduct
import org.burgas.webelectronics.entity.pk.StoreProductPK
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StoreProductRepository : JpaRepository<StoreProduct, StoreProductPK>