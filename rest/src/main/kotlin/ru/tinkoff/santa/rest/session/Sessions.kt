package ru.tinkoff.santa.rest.session

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.timestamp

object Sessions : IntIdTable() {
    var guid = uuid("guid")
    var currentState = customEnumeration(
        "current_state",
        "session_state",
        { value -> SessionState.valueOf(value as String) },
        { PostgresCustomEnum("session_state", it) })
    var description = text("description").nullable()
    var hostId = integer("host_id")
    var minPlayersQuantity = integer("min_players_quantity")
    var eventTimestamp = timestamp("event_timestamp")
    var timestampToChoose = timestamp("timestamp_to_choose")
}