package ru.tinkoff.santa.rest.gift

import org.jetbrains.exposed.dao.id.IntIdTable

object Gifts : IntIdTable() {
    val pictureUrl = text("picture_url").nullable()
    val name = text("name")
    val description = text("description").nullable()
}