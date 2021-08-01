package ru.tinkoff.santa.rest.guid

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import ru.tinkoff.sanata.shared_models.request.ConnectGuidRequest
import ru.tinkoff.sanata.shared_models.request.CreateGuidRequest
import ru.tinkoff.santa.rest.user.UserService
import java.util.*

fun Application.guidModule() {
    val guidService: GuidService by closestDI().instance()
    val userService: UserService by closestDI().instance()

    routing{
        route("/guid"){
            route("/create"){
                post{
                    val request = call.receive<CreateGuidRequest>()
                    call.respond(HttpStatusCode.Created, guidService.create(request.telegramId).toString())
                }
            }

            route("/connect"){
                post{
                    val request = call.receive<ConnectGuidRequest>()
                    val guid = guidService.getByGuid(UUID.fromString(request.telegramGuid))
                    if (guid != null) {
                        userService.setTelegramId(request.id, guid.telegramId, guid.telegramGuid)
                        guidService.delete(guid.telegramGuid)
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }
        }
    }
}