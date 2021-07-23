package ru.rsreu

import com.typesafe.config.ConfigFactory
import configureRouting
import configureSerialization
import io.github.config4k.extract
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.rsreu.bot.SantaBot

fun main() {
    val config = ConfigFactory.load().extract<AppConfig>()
    val bot = SantaBot(config.telegram)
    val engine = embeddedServer(Netty, port = config.http.port) {
        bot.start()
        configureRouting(bot)
        configureSerialization()
    }
    engine.start()
}
