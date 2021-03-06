package ru.tinkoff.santa.rest.user_session_gift

import org.jetbrains.exposed.dao.id.IntIdTable

object UsersSessionsGifts : IntIdTable(name = "users_sessions_gifts") {
    val userSessionId = integer("user_session_id")
    val giftId = integer("gift_id")
}