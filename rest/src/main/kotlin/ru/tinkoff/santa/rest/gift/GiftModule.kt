package ru.tinkoff.santa.rest.gift

import io.ktor.application.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.singleton

fun Application.giftModule() {
    val service: GiftService by closestDI().instance()

}

fun DI.Builder.giftComponents() {
    bind<GiftDao>() with singleton { GiftDao(instance()) }
    bind<GiftService>() with singleton { GiftService(instance()) }
}