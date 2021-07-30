package ru.tinkoff.santa.rest.gift

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import ru.tinkoff.sanata.shared_models.request.CreateGiftRequest

fun Application.giftModule() {
    val giftController: GiftController by closestDI().instance()

    routing {
        route("/gift") {
            route("/create") {
                post {
                    runCatching {
                        call.receive<CreateGiftRequest>()
                    }.onSuccess {
                        call.respond(
                            HttpStatusCode.Created,
                            giftController.create(it.userId, it.sessionId, it.giftName, it.giftDescription)
                        )
                    }.onFailure {
                        throw IllegalArgumentException()
                    }
                }
            }

            route("/delete/{id}") {
                delete {
                    val giftId = call.parameters["id"]?.toInt()
                    if (giftId != null) {
                        giftController.delete(giftId)
                        call.respond(HttpStatusCode.OK)
                    } else {
                        throw IllegalArgumentException()
                    }
                }
            }
        }
    }
}