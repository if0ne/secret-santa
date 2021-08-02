package ru.tinkoff.santa.rest.gift

import ru.tinkoff.sanata.shared_models.model.Gift
import ru.tinkoff.santa.rest.gift.exception.GiftNotFoundException

class GiftService(private val giftDao: GiftDao) {
    fun getAll(): List<Gift> = giftDao.getAll()

    fun getById(id: Int): Gift? = giftDao.getById(id)

    fun create(pictureUrl: String?, name: String, description: String?): Gift =
        giftDao.create(pictureUrl, name, description)

    fun update(id: Int, pictureUrl: String?, name: String, description: String?) =
        giftDao.update(id, pictureUrl, name, description)

    fun checkGift(giftId: Int) {
        if (getById(giftId) == null) {
            throw GiftNotFoundException()
        }
    }

    fun delete(id: Int) = giftDao.delete(id)
}