package ru.tinkoff.santa.rest.session

import kotlinx.serialization.Serializable
import ru.tinkoff.santa.rest.serialization.LocalDateTimeSerializer
import ru.tinkoff.santa.rest.serialization.UUIDSerializer
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