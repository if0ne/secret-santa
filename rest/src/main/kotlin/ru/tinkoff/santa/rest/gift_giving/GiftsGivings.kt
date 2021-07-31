package ru.tinkoff.santa.rest.gift_giving

import org.jetbrains.exposed.dao.id.IntIdTable

object GiftsGivings : IntIdTable(name = "gift_giving") {
    val sessionId = integer("session_id")
    val giftGivingUserId = integer("gift_giving_user_id")
    val giftReceivingUserId = integer("gift_receiving_user_id")
}