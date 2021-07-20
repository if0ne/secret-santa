package ru.rsreu

import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.rsreu.plugins.configureRouting
import ru.rsreu.plugins.configureSerialization

fun main() {
    val config = ConfigFactory.load().extract<AppConfig>()
    val tgBot = TgBot(config.telegram)
    val engine = embeddedServer(Netty, port = config.http.port) {
        tgBot.start()
        configureRouting(tgBot)
        configureSerialization()
    }
    engine.start()
}
