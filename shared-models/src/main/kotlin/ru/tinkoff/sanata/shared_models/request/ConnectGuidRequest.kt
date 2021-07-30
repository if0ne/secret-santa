package ru.tinkoff.sanata.shared_models.request

import kotlinx.serialization.Serializable

@Serializable
data class ConnectGuidRequest(
    val id: Int,
    val telegramGuid: String
)