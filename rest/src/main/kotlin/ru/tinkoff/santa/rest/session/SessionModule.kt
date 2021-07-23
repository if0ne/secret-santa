package ru.tinkoff.santa.rest.session

import io.ktor.application.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.sessionModule() {
    val sessionService: SessionService by closestDI().instance()

}