package ru.tinkoff.santa.rest.user

import io.ktor.application.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.singleton

fun Application.userModule() {
    val service: UserService by closestDI().instance()
}

fun DI.Builder.userComponents() {
    bind<UserDao>() with singleton { UserDao(instance()) }
    bind<UserService>() with singleton { UserService(instance()) }
}