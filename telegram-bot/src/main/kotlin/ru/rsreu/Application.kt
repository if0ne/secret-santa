package ru.rsreu

import com.typesafe.config.ConfigFactory
import configureRouting
import configureSerialization
import io.github.config4k.extract
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.rsreu.bot.SantaBot

fun main() {
    val config = ConfigFactory.load().extract<AppConfig>()
    val client = HttpClient(CIO) {
        install(JsonFeature)
        expectSuccess = false
    }
    val bot = SantaBot(config, client)
    val engine = embeddedServer(Netty, port = config.http.port) {
        bot.start()
        configureRouting(bot)
        configureSerialization()
    }
    engine.start()
}
