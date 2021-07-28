package ru.tinkoff.santa.rest.guid

import java.util.*

data class Guid(
    val telegramGuid: UUID,
    val telegramId: Long
)