package ru.tinkoff.santa.rest.user_session_gift

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.userSessionGiftComponents() {
    bind<UserSessionGiftDao>() with singleton { instance() }
    bind<UserSessionGiftService>() with singleton { instance() }
}