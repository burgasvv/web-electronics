package org.burgas.webelectronics.message

enum class CategoryMessages {

    CATEGORY_NOT_FOUND("Category not found"),
    NAME_FIELD_EMPTY("name field is empty"),
    DESCRIPTION_FIELD_EMPTY("Description field is empty");

    val message: String

    constructor(message: String) {
        this.message = message
    }
}