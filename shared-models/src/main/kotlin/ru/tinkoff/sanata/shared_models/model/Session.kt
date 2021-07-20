package ru.tinkoff.sanata.shared_models.model

import kotlinx.serialization.Serializable
import ru.tinkoff.sanata.shared_models.model.serialization.LocalDateTimeSerializer
import ru.tinkoff.sanata.shared_models.model.serialization.UUIDSerializer
import java.util.*
import java.time.LocalDateTime

@Serializable
data class Session(
    val id: Int,
    @Serializable(with = UUIDSerializer::class)
    val guid: UUID?,
    val currentState: SessionState,
    val description: String?,
    val hostId: Int,
    val budget: Int,
    val minPlayersQuantity: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val eventTimestamp: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val timestampToChoose: LocalDateTime
)