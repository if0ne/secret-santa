package ru.tinkoff.santa.rest.guid

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.Builder.guidComponents() {
    bind<GuidDao>() with singleton { GuidDao(instance()) }
    bind<GuidService>() with singleton { GuidService(instance()) }
}