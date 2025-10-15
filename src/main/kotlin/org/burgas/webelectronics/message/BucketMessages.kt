package org.burgas.webelectronics.message

enum class BucketMessages {

    BUCKET_NOT_FOUND("Bucket not found");

    val message: String

    constructor(message: String) {
        this.message = message
    }
}