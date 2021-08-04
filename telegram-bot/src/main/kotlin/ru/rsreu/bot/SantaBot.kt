package ru.rsreu.bot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.webhook
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import ru.rsreu.AppConfig
import ru.tinkoff.sanata.shared_models.model.Session
import ru.tinkoff.sanata.shared_models.model.SessionState
import ru.tinkoff.sanata.shared_models.model.User
import ru.tinkoff.sanata.shared_models.request.CreateGuidRequest
import ru.tinkoff.sanata.shared_models.request.LeaveRequest
import ru.tinkoff.sanata.shared_models.request.UserSessionInfoRequest
import ru.tinkoff.sanata.shared_models.status.GuidErrorCode

class SantaBot(config: AppConfig, client: HttpClient) {
    private val telegramBotController = TelegramBotController()

    private val telegramBot = bot {
        token = config.telegram.token
        webhook {
            url = config.telegram.webhookUrl
        }
        dispatch {
            command("start") {
                telegramBotController.sendWelcomeMessage(bot, message.chat.id)
            }

            callbackQuery("register") {
                val id = callbackQuery.from.id
                runBlocking {
                    val response = client.post<HttpResponse>(config.server.url + config.server.guidCreateRoute) {
                        method = HttpMethod.Post
                        contentType(ContentType.Application.Json)
                        body = CreateGuidRequest(id)
                    }
                    when (response.status) {
                        HttpStatusCode.Created -> {
                            telegramBotController.sendGuid(bot, callbackQuery, response.receive())
                        }
                        HttpStatusCode.InternalServerError -> {
                            when (response.receive<GuidErrorCode>()) {
                                GuidErrorCode.ACCOUNT_ALREADY_LINKED -> {
                                    telegramBotController.sendMessageAboutLinkedAccount(bot, id)
                                }
                                GuidErrorCode.GUID_ALREADY_ISSUED -> {
                                    telegramBotController.sendMessageAboutAlreadyIssued(bot, id)
                                }
                                else -> {
                                }
                            }
                        }
                    }
                }
            }

            command("features") {
                telegramBotController.sendFeatures(bot, message.chat.id)
            }

            callbackQuery("userInfo") {
                val id = callbackQuery.from.id
                runBlocking {
                    val response =
                        client.get<HttpResponse>(config.server.url + config.server.getUserInfoRoute + id.toString()) {
                            method = HttpMethod.Get
                            contentType(ContentType.Application.Json)
                        }
                    when (response.status) {
                        HttpStatusCode.OK -> {
                            telegramBotController.sendUserInfo(bot, id, response.receive())
                        }
                        HttpStatusCode.NotFound -> {
                            telegramBotController.sendMessageAboutNotLinkedAccount(bot, id)
                        }
                    }
                }
            }

            callbackQuery("sessions") {
                val id = callbackQuery.from.id
                runBlocking {
                    val userResponse =
                        client.get<HttpResponse>(config.server.url + config.server.getUserInfoRoute + id.toString()) {
                            method = HttpMethod.Get
                            contentType(ContentType.Application.Json)
                        }
                    when (userResponse.status) {
                        HttpStatusCode.OK -> {
                            val user = userResponse.receive<User>()
                            runBlocking {
                                val response =
                                    client.get<HttpResponse>(config.server.url + config.server.getSessionsRoute + id.toString()) {
                                        method = HttpMethod.Get
                                        contentType(ContentType.Application.Json)
                                    }
                                when (response.status) {
                                    HttpStatusCode.OK -> {
                                        val activeSessions = response.receive<List<Session>>()
                                            .filter { it.currentState != SessionState.FINISH }
                                        telegramBotController.sendSessions(bot, user, activeSessions)
                                    }
                                    else -> {
                                    }
                                }
                            }
                        }
                        HttpStatusCode.NotFound -> telegramBotController.sendMessageAboutNotLinkedAccount(bot, id)
                    }
                }
            }

            callbackQuery("leave") {
                val userId = callbackQuery.data.split(" ")[1].toInt()
                val sessionId = callbackQuery.data.split(" ")[2].toInt()
                bot.deleteLastMessage(callbackQuery)
                runBlocking {
                    val response =
                        client.delete<HttpResponse>(config.server.url + config.server.leaveRoute) {
                            method = HttpMethod.Delete
                            contentType(ContentType.Application.Json)
                            body = LeaveRequest(userId, sessionId)
                        }
                    when (response.status) {
                        HttpStatusCode.OK -> telegramBotController.sendMessageAboutSuccessLeave(bot, callbackQuery.from.id, sessionId)
                    }
                }
            }

            callbackQuery("userSessionInfo") {
                val userId = callbackQuery.data.split(" ")[1].toInt()
                val sessionId = callbackQuery.data.split(" ")[2].toInt()
                bot.deleteLastMessage(callbackQuery)
                runBlocking {
                    val response =
                        client.post<HttpResponse>(config.server.url + config.server.getUserInfoAboutSessionRoute) {
                            method = HttpMethod.Post
                            contentType(ContentType.Application.Json)
                            body = UserSessionInfoRequest(userId, sessionId)
                        }
                    when (response.status) {
                        HttpStatusCode.OK -> telegramBotController.sendUserSessionInfo(bot, response.receive())
                    }
                }
            }

            callbackQuery("addGift"){
                val userId = callbackQuery.data.split(" ")[1].toInt()
                val sessionId = callbackQuery.data.split(" ")[2].toInt()
                telegramBotController.startCreatingGift(bot, callbackQuery.from.id, userId, sessionId)
            }

            callbackQuery("create") {
                telegramBotController.startCreatingSession(bot, callbackQuery.from.id)
            }

            callbackQuery("confirmCreating") {
                val id = callbackQuery.from.id
                val creatingSessionRequest = telegramBotController.getCreateSessionRequest(id)
                runBlocking {
                    val response = client.post<HttpResponse>(config.server.url + config.server.createSessionRoute) {
                        method = HttpMethod.Post
                        contentType(ContentType.Application.Json)
                        body = creatingSessionRequest
                    }
                    when (response.status) {
                        HttpStatusCode.Created -> {
                            telegramBotController.successCreateSession(bot, id, callbackQuery)
                        }
                        else -> {
                        }
                    }
                }
            }

            callbackQuery("cancelCreating") {
                telegramBotController.cancelSessionCreating(bot, callbackQuery)
            }

            text {
                val text = message.text!!
                if (!text.startsWith("/")) {
                    telegramBotController.enterText(bot, message.from!!.id, text)
                }
            }
        }
    }

    fun start() = telegramBot.startWebhook()

    fun processUpdate(receiveBody: String) = telegramBot.processUpdate(receiveBody)

    fun notifyAboutEndSession(session: Session, users: List<User>) {
        users.filter { it.telegramId != null }.forEach {
            telegramBotController.notifyAboutEndSession(telegramBot, it, session)
        }
    }

    fun notifyAboutStartSession(session: Session, users: List<User>) {
        users.filter { it.telegramId != null }.forEach {
            telegramBotController.notifyAboutStartSession(telegramBot, it, session)
        }
    }
}