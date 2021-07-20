package ru.rsreu.plugins

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.rsreu.TgBot

fun Application.configureRouting(bot: TgBot) {

    routing {
        post("/") {
            val receiveBody = call.receiveText()
            bot.processUpdate(receiveBody)
            call.respond(HttpStatusCode.OK)
        }
    }
}
