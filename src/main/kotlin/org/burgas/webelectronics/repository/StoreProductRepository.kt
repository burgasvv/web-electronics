package org.burgas.webelectronics.repository

import org.burgas.webelectronics.entity.pk.StoreProduct
import org.burgas.webelectronics.entity.pk.StoreProductPK
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StoreProductRepository : JpaRepository<StoreProduct, StoreProductPK> {

    @Query(
        nativeQuery = true,
        value = """
            select sp.* from store_product sp where sp.product_id = :productId
        """
    )
    fun findStoreProductsByProductId(productId: UUID): List<StoreProduct>
}