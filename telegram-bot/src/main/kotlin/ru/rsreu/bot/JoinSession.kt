package ru.rsreu.bot

data class JoinSession(
    val telegramId: Long,
    var sessionGuid: String? = null
)
