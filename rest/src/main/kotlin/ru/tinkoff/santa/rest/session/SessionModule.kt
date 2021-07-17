package ru.tinkoff.santa.rest.session

import io.ktor.application.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.singleton

fun Application.sessionModule() {
    val service: SessionService by closestDI().instance()
}

fun DI.Builder.sessionComponents() {
    bind<SessionDao>() with singleton { SessionDao(instance()) }
    bind<SessionService>() with singleton { SessionService(instance()) }
}