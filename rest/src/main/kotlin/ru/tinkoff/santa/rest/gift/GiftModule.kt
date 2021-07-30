package ru.tinkoff.santa.rest.gift

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import ru.tinkoff.sanata.shared_models.request.CreateGiftRequest
import ru.tinkoff.santa.rest.user_session.UserSessionService
import ru.tinkoff.santa.rest.user_session_gift.UserSessionGiftService

fun Application.giftModule() {
    val giftService: GiftService by closestDI().instance()
    val userSessionGiftService: UserSessionGiftService by closestDI().instance()
    val userSessionService: UserSessionService by closestDI().instance()

    routing {
        route("/gift") {
            route("/create") {
                post {
                    val request = call.receive<CreateGiftRequest>()
                    val giftId = giftService.create(null, request.giftName, request.giftDescription)
                    val userSession = userSessionService.getByUserIdAndSessionId(request.userId, request.sessionId)
                    if (userSession != null) {
                        userSessionGiftService.create(userSession.id, giftId)
                        call.respond(HttpStatusCode.Created)
                    }
                }
            }

            route("/delete/{id}") {
                delete {
                    val giftId = call.parameters["id"]?.toInt()
                    if (giftId != null) {
                        val userSessionGift = userSessionGiftService.getByGiftId(giftId)
                        if (userSessionGift != null) {
                            userSessionGiftService.delete(userSessionGift.id)
                        }
                        giftService.delete(giftId)
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }
        }
    }
}