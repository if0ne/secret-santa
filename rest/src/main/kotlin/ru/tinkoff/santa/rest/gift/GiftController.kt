package ru.tinkoff.santa.rest.gift

import ru.tinkoff.sanata.shared_models.model.Gift
import ru.tinkoff.santa.rest.session.SessionService
import ru.tinkoff.santa.rest.user.UserService
import ru.tinkoff.santa.rest.user_session.UserSessionService
import ru.tinkoff.santa.rest.user_session_gift.UserSessionGiftService

class GiftController(
    private val giftService: GiftService,
    private val userService: UserService,
    private val sessionService: SessionService,
    private val userSessionService: UserSessionService,
    private val userSessionGiftService: UserSessionGiftService
) {
    fun create(userId: Int, sessionId: Int, name: String, description: String?): Gift {
        if (userService.getById(userId) == null) {
            throw Exception()
        }
        if (sessionService.getById(sessionId) == null) {
            throw Exception()
        }
        val userSession = userSessionService.getByUserIdAndSessionId(userId, sessionId)
        if (userSession != null) {
            val gift = giftService.create(
                null,
                name,
                description
            )
            userSessionGiftService.create(
                userSession.id, gift.id
            )
            return gift
        } else {
            throw Exception()
        }
    }

    fun delete(giftId: Int) {
        if (giftService.getById(giftId) == null) {
            throw Exception()
        }
        userSessionGiftService.deleteByGift(giftId)
        giftService.delete(giftId)
    }
}