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
import java.util.*

fun Application.guidModule() {
    val guidController: GuidController by closestDI().instance()

    routing {
        route("/guid") {
            route("/create") {
                post {
                    runCatching {
                        call.receive<CreateGuidRequest>()
                    }.onSuccess {
                        call.respond(
                            HttpStatusCode.Created,
                            guidController.create(it.telegramId).telegramGuid.toString()
                        )
                    }.onFailure {
                        throw IllegalArgumentException()
                    }
                }
            }

            route("/connect") {
                post {
                    runCatching {
                        call.receive<ConnectGuidRequest>()
                    }.onSuccess {
                        guidController.connect(UUID.fromString(it.telegramGuid), it.id)
                        call.respond(HttpStatusCode.OK)
                    }.onFailure {
                        throw IllegalArgumentException()
                    }
                }
            }
        }
    }
}