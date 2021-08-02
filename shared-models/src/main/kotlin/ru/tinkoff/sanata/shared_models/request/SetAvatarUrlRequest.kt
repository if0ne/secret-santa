package ru.tinkoff.sanata.shared_models.request

import kotlinx.serialization.Serializable

@Serializable
data class SetAvatarUrlRequest(
    val userId: Int,
    val avatarUrl: String
)
