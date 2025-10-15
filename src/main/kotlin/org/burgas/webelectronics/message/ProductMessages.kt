package org.burgas.webelectronics.message

enum class ProductMessages {

    PRODUCT_AMOUNT_NOT_FOUND("Product amount not found"),
    PRODUCT_AMOUNT_NOT_ENOUGH("Product amount not enough"),
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