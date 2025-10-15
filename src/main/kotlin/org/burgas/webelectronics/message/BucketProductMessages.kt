package org.burgas.webelectronics.message

enum class BucketProductMessages {

    BUCKET_PRODUCT_WRONG_AMOUNT("Bucket product wrong amount"),
    BUCKET_PRODUCT_NOT_FOUND("Bucket product not found");

    val message: String

    constructor(message: String) {
        this.message = message
    }
}