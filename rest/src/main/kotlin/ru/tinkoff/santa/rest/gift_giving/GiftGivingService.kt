package ru.tinkoff.santa.rest.gift_giving

class GiftGivingService(private val giftGivingDao: GiftGivingDao) {
    fun getAll(): List<GiftGiving> = giftGivingDao.getAll()

    fun create(sessionId: Int, giftGivingUserId: Int, giftReceivingUserId: Int): GiftGiving =
        giftGivingDao.create(sessionId, giftGivingUserId, giftReceivingUserId)

    fun delete(id: Int) = giftGivingDao.delete(id)
}