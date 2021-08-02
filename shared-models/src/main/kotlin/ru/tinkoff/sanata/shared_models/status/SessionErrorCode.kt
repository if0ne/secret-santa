package ru.tinkoff.sanata.shared_models.status

enum class SessionErrorCode {
    SESSION_NOT_FOUND,
    SESSION_NOT_STARTED,
    SESSION_ALREADY_STARTED,
    SESSION_ALREADY_FINISHED,
    NOT_ENOUGH_USERS_FOR_START,
    USER_NOT_IN_SESSION,
    USER_ALREADY_IN_SESSION
}