package ru.tinkoff.santa.rest.gift

import io.ktor.application.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.giftModule() {
    val service: GiftService by closestDI().instance()

}