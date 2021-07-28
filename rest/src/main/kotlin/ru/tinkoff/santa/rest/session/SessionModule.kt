package ru.tinkoff.santa.rest.session

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import ru.tinkoff.sanata.shared_models.model.SessionState
import ru.tinkoff.sanata.shared_models.request.CreateSessionRequest
import ru.tinkoff.sanata.shared_models.request.JoinRequest
import ru.tinkoff.santa.rest.user.UserService
import ru.tinkoff.santa.rest.user_session.UserSessionService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Application.sessionModule() {
    val sessionService: SessionService by closestDI().instance()
    val userSessionService: UserSessionService by closestDI().instance()
    val userService: UserService by closestDI().instance()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    routing{
        route("/session"){
            route("/create"){
                post{
                    val request = call.receive<CreateSessionRequest>()
                    val hostId = request.hostId
                    var id:Int = 0
                    if(hostId != null){
                        id = hostId
                    } else {
                        val telegramHostId = request.hostTelegramId
                        if(telegramHostId != null){
                            val user = userService.getByTelegramId(telegramHostId)
                            if(user != null) {
                                id = user.id
                            }
                        }
                    }
                    sessionService.create(
                        SessionState.LOBBY,
                        request.description,
                        id,
                        request.budget,
                        LocalDateTime.parse(request.eventDateTime, formatter),
                        LocalDateTime.parse(request.dateTimeToChoose, formatter),
                        request.minPlayersQuantity!!
                    )
                    userSessionService.create(
                        id,
                        sessionService.getByHostId(id).last().id
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

            route("/tg_user/{id}"){
                get{
                    val user = userService.getByTelegramId(call.parameters["id"]!!.toLong())

                    if (user != null) {
                        val userSession = userSessionService.getByUserId(user.id)
                        val sessions = userSession.map {
                            sessionService.getById(it.sessionId)
                        }
                        call.respond(HttpStatusCode.OK, sessions)
                    }
                }
            }
        }
    }

}