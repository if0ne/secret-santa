package ru.tinkoff.santa.rest.gift_giving

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.giftGivingComponents() {
    bind<GiftGivingDao>() with singleton { GiftGivingDao(instance()) }
    bind<GiftGivingService>() with singleton { GiftGivingService(instance()) }
}