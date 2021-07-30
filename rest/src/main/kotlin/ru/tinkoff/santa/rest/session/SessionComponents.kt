package ru.tinkoff.santa.rest.session

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

class SessionComponents

fun DI.Builder.sessionComponents() {
    bind<SessionDao>() with singleton { SessionDao(instance()) }
    bind<SessionService>() with singleton { SessionService(instance()) }
    bind<SessionController>() with singleton { SessionController(instance(), instance(), instance()) }
}