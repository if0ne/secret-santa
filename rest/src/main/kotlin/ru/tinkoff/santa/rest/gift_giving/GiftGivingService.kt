package ru.tinkoff.santa.rest.gift_giving

class GiftGivingService(private val giftGivingDao: GiftGivingDao) {
    fun getAll(): List<GiftGiving> = giftGivingDao.getAll()
}