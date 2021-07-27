package ru.tinkoff.santa.rest.user

import ru.tinkoff.santa.rest.session.SessionService
import ru.tinkoff.santa.rest.user_session.UserSessionService
import ru.tinkoff.santa.rest.user_session_gift.UserSessionGiftService

class UserController(
    private val userService: UserService,
    private val sessionService: SessionService,
    private val userSessionService: UserSessionService,
    private val userSessionGiftService: UserSessionGiftService
) {
}