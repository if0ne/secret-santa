package ru.tinkoff.santa.rest.session

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import ru.tinkoff.sanata.shared_models.model.SessionState
import ru.tinkoff.sanata.shared_models.request.CreateRequest
import ru.tinkoff.sanata.shared_models.request.JoinRequest
import ru.tinkoff.santa.rest.user_session.UserSessionDao
import ru.tinkoff.santa.rest.user_session.UserSessionService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun Application.sessionModule() {
    val sessionService: SessionService by closestDI().instance()
    val userSessionService: UserSessionService by closestDI().instance()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    routing{
        route("/session"){
            route("/create"){
                post{
                    val request = call.receive<CreateRequest>()
                    sessionService.create(
                        SessionState.LOBBY,
                        request.description,
                        request.hostId,
                        request.budget,
                        LocalDateTime.parse(request.eventDateTime, formatter),
                        LocalDateTime.parse(request.dateTimeToChoose, formatter),
                        request.minPlayersQuantity!!
                    )
                    userSessionService.create(
                        request.hostId,
                        sessionService.getByHostId(request.hostId).last().id
                    )
                    call.respond(HttpStatusCode.Created)
                }
            }

            route("/join"){
                post{
                    val request = call.receive<JoinRequest>()
                    userSessionService.create(request.userId, request.sessionId)
                    call.respond(HttpStatusCode.OK)
                }
            }

            route("/user/{id}"){
                get{
                    call.respond(HttpStatusCode.OK, userSessionService.getByUserId(call.parameters["id"]!!.toInt()))
                }
            }
        }
    }

}