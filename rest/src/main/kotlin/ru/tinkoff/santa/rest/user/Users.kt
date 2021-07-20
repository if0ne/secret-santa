package ru.tinkoff.santa.rest.user

import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable() {
    val telegramGuid = uuid("guid").nullable()
    val nickname = varchar("nickname", 30)
    val email = varchar("email", 254)
    val password = binary("password")
    val firstName = text("first_name")
    val lastName = text("last_name")
    val middleName = text("middle_name").nullable()
    val avatarUrl = text("avatar_url").nullable()
    val telegramId = long("telegram_id").nullable()
}