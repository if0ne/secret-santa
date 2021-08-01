package ru.tinkoff.santa.rest.guid

import java.util.*

class GuidService(private val guidDao: GuidDao) {
    fun getAll(): List<Guid> = guidDao.getAll()

    fun create(telegramId: Long): Guid = guidDao.create(telegramId)

    fun getByGuid(guid: UUID): Guid? = guidDao.getByGuid(guid)

    fun getByTelegramId(telegramId: Long): Guid? = guidDao.getByTelegramId(telegramId)

    fun delete(guid: UUID) = guidDao.deleteByGuid(guid)
}