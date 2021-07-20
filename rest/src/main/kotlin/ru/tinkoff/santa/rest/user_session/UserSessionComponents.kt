package ru.tinkoff.santa.rest.user_session

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.userSessionComponents() {
    bind<UserSessionDao>() with singleton { UserSessionDao(instance()) }
    bind<UserSessionService>() with singleton { UserSessionService(instance()) }
}