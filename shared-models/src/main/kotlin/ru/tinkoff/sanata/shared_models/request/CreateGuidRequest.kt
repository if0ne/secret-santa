package ru.tinkoff.sanata.shared_models.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateGuidRequest(val telegramId: Long)
