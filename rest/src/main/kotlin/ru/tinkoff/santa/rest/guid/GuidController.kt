package ru.tinkoff.santa.rest.guid

import ru.tinkoff.sanata.shared_models.status.GuidErrorCode
import ru.tinkoff.santa.rest.guid.exception.GuidException
import ru.tinkoff.santa.rest.user.UserService
import java.util.*

class GuidController(
    private val guidService: GuidService,
    private val userService: UserService
) {
    fun create(telegramId: Long): Guid {
        if (userService.getByTelegramId(telegramId) != null) {
            throw GuidException(GuidErrorCode.ACCOUNT_ALREADY_LINKED)
        }
        if (guidService.getByTelegramId(telegramId) != null) {
            throw GuidException(GuidErrorCode.GUID_ALREADY_ISSUED)
        }
        return guidService.create(telegramId)
    }

    fun connect(telegramGuid: UUID, userId: Int): Long {
        userService.checkUser(userId)
        val guid = guidService.getByGuid(telegramGuid) ?: throw GuidException(GuidErrorCode.GUID_NOT_FOUNDED)
        userService.setTelegramId(userId, guid.telegramId)
        guidService.delete(telegramGuid)
        return guid.telegramId
    }
}