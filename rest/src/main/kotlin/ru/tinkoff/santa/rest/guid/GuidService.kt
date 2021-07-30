package ru.tinkoff.santa.rest.guid

import java.util.*

class GuidService(private val guidDao: GuidDao) {
    fun getAll(): List<Guid> = guidDao.getAll()

    fun create(telegramId: Long): UUID = guidDao.create(telegramId)

    fun getByGuid(guid: UUID): Guid? = guidDao.getByGuid(guid)

    fun delete(guid: UUID) = guidDao.delete(guid)
}