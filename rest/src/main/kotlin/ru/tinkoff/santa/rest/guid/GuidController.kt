package ru.tinkoff.santa.rest.guid

import ru.tinkoff.santa.rest.user.UserService
import java.util.*

class GuidController(
    private val guidService: GuidService,
    private val userService: UserService
) {
    fun create(telegramId: Long): Guid {
        if (userService.getByTelegramId(telegramId) != null) {
            throw Exception()
        }
        if (guidService.getByTelegramId(telegramId) != null) {
            throw Exception()
        }
        return guidService.create(telegramId)
    }

    fun connect(telegramGuid: UUID, userId: Int) {
        val guid = guidService.getByGuid(telegramGuid) ?: throw Exception()
        userService.setTelegramId(userId, guid.telegramId, guid.telegramGuid)
        guidService.delete(telegramGuid)
    }
}