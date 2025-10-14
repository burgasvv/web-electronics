package org.burgas.webelectronics.message

enum class ProductMessages {

    PRODUCT_NOT_FOUND("Product not found"),
    CATEGORY_FIELD_EMPTY("Category field is empty"),
    NAME_FIELD_EMPTY("Name field is empty"),
    DESCRIPTION_FIELD_EMPTY("Description field is empty"),
    PRICE_FIELD_EMPTY("Price field is empty");

    val message: String

    constructor(message: String) {
        this.message = message
    }
}