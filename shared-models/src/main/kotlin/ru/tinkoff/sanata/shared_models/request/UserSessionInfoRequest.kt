package ru.tinkoff.sanata.shared_models.request

import kotlinx.serialization.Serializable

@Serializable
data class UserSessionInfoRequest(
    val userId: Int,
    val sessionId: Int
)
