package ru.tinkoff.santa.rest.user_session

import org.jetbrains.exposed.dao.id.IntIdTable

object UsersSessions : IntIdTable() {
    val userId = integer("user_id")
    val sessionId = integer("session_id")
}