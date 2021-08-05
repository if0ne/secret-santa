package ru.rsreu.bot

data class AdditionGift(
    val telegramId: Long,
    val userId: Int,
    val sessionId: Int,
    var giftName: String? = null,
    var giftDescription: String? = null
)
