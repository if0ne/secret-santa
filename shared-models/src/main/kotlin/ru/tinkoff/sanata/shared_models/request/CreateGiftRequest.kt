package ru.tinkoff.sanata.shared_models.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateGiftRequest(
    val userId: Int,
    val sessionId: Int,
    val giftName: String,
    val giftDescription: String?
)
