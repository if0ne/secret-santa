package ru.rsreu.bot

data class SessionCreating(
    val telegramId: Long,
    var state: SessionCreatingState,
    var description: String? = null,
    var budget: Int? = null,
    var minPlayersQuantity: Int? = null,
    var eventTimestamp: String? = null,
    var timestampToChoose: String? = null
)
