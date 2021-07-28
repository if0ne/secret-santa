package ru.tinkoff.sanata.shared_models.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateSessionRequest(
    val description: String?,
    val hostId: Int?,
    val hostTelegramId: Long?,
    val budget: Int,
    val eventDateTime: String,
    val dateTimeToChoose: String,
    val minPlayersQuantity: Int?,
)