package org.burgas.webelectronics.message

enum class ImageMessages {

    IMAGE_NOT_FOUND("Image not found");

    val message: String

    constructor(message: String) {
        this.message = message
    }
}