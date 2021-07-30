package ru.tinkoff.santa.rest.gift_giving

import kotlinx.serialization.Serializable

@Serializable
data class GiftGiving(
    val id: Int,
    val sessionId: Int,
    val giftGivingUserId: Int,
    val giftReceivingUserId: Int
)
