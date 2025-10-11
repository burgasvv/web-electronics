package org.burgas.webelectronics.message

enum class IdentityMessages {

    NOT_AUTHENTICATED("Identity not authenticated"),
    NOT_AUTHORIZED("Identity not authorized"),
    ENABLED_MATCHES("Enabled flag matches"),
    SAME_PASSWORD("Password and new password matches"),
    AUTHORITY_FIELD_EMPTY("Authority field is empty"),
    EMAIL_FIELD_EMPTY("Email field is empty"),
    PASSWORD_FIELD_EMPTY("Password field is empty"),
    FIRST_NAME_FIELD_EMPTY("First Name field is empty"),
    LAST_NAME_FIELD_EMPTY("Last Name field is empty"),
    PATRONYMIC_FIELD_EMPTY("Patronymic field is empty"),
    IDENTITY_NOT_FOUND("identity not found");

    val message: String

    constructor(message: String) {
        this.message = message
    }
}