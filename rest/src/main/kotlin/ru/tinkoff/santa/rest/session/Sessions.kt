package ru.tinkoff.santa.rest.session

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.timestamp

object Sessions : IntIdTable() {
    val guid = uuid("guid")
    val currentState = customEnumeration(
        "current_state",
        "session_state",
        { value -> SessionState.valueOf(value as String) },
        { PostgresCustomEnum("session_state", it) })
    val description = text("description").nullable()
    val hostId = integer("host_id")
    val budget = integer("budget")
    val minPlayersQuantity = integer("min_players_quantity")
    val eventTimestamp = timestamp("event_timestamp")
    val timestampToChoose = timestamp("timestamp_to_choose")
}