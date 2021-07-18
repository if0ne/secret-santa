package ru.tinkoff.santa.rest.session

import java.sql.Timestamp
import java.util.*

data class Session(
    val id: Int,
    val guid: UUID,
    val currentState: SessionState,
    val description: String?,
    val hostId: Int,
    val budget: Int,
    val minPlayersQuantity: Int,
    val eventTimestamp: Timestamp,
    val timestampToChoose: Timestamp
)