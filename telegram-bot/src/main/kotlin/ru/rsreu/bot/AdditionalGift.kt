package ru.rsreu.bot

data class AdditionalGift(
    val telegramId: Long,
    val userId: Int,
    val sessionId: Long,
    val giftName: String?,
    val giftDescription: String?
)
