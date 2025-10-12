package org.burgas.webelectronics.message

enum class StoreMessages {

    STORE_NOT_FOUND("Store not found");

    val message: String

    constructor(message: String) {
        this.message = message
    }
}