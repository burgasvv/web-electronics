package org.burgas.webelectronics.message

enum class ImageMessages {

    PART_NOPT_FOUND("Part not found"),
    IMAGE_NOT_FOUND("Image not found");

    val message: String

    constructor(message: String) {
        this.message = message
    }
}