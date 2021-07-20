package ru.tinkoff.sanata.shared_models.status

import kotlinx.serialization.Serializable

@Serializable
enum class AuthenticationStatusCode(code: Int) {
    NONE(0),
    USER_NAME_NOT_EXISTS(1),
    INVALID_PASSWORD(2),
    SUCCESS(3)
}