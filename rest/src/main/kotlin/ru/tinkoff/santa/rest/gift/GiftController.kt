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
        userService.checkUser(userId)
        sessionService.checkSession(sessionId)
        val userSession = userSessionService.checkAndGetUserSession(userId, sessionId)
        val gift = giftService.create(
            null,
            name,
            description
        )
        userSessionGiftService.create(
            userSession.id, gift.id
        )
        return gift
    }

    fun delete(giftId: Int) {
        giftService.checkGift(giftId)
        userSessionGiftService.deleteByGift(giftId)
        giftService.delete(giftId)
    }
}