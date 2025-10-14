package org.burgas.webelectronics.message

enum class StoreProductMessages {

    STORE_PRODUCT_NOT_FOUND("Store product not found");

    val message: String

    constructor(message: String) {
        this.message = message
    }
}