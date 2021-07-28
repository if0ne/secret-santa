package ru.tinkoff.santa.rest.guid

import org.jetbrains.exposed.sql.Table

object Guids : Table(name = "guid_tg") {
    val telegramGuid = uuid("telegram_guid")
    val telegramId = long("telegram_id")
}