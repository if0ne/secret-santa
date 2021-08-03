package ru.rsreu.bot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
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
import ru.tinkoff.sanata.shared_models.request.UserSessionInfoRequest
import ru.tinkoff.sanata.shared_models.response.UserInfoAboutSessionResponse
import ru.tinkoff.sanata.shared_models.status.GuidErrorCode

class SantaBot(config: AppConfig, client: HttpClient) {

    private val buttons = TgButtons()

    private val telegramBot = bot {
        token = config.telegram.token
        webhook {
            url = config.telegram.webhookUrl
        }
        dispatch {
            command("start") {
                bot.sendMsg(
                    chatId = message.chat.id,
                    text = "Добро пожаловать в Тайного Санту",
                    buttons = buttons.getButtons(ButtonsType.START_BUTTONS)
                )
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
                            bot.deleteLastMessage(callbackQuery)
                            bot.sendSomeMsg(
                                id,
                                "Ваш GUID:",
                                response.receive(),
                                "Укажите его в своем личном кабинете, чтобы привязать аккаунт ... ."
                            )
                            bot.sendMsg(
                                id,
                                "Возможности бота:",
                                buttons.getButtons(ButtonsType.FUNCTIONAL_BUTTONS)
                            )
                        }
                        HttpStatusCode.InternalServerError -> {
                            when (response.receive<GuidErrorCode>()) {
                                GuidErrorCode.ACCOUNT_ALREADY_LINKED -> {
                                    bot.sendMsg(
                                        id,
                                        "Ваш аккаунт уже привязан. Возможности бота: ",
                                        buttons.getButtons(ButtonsType.FUNCTIONAL_BUTTONS)
                                    )
                                }
                                GuidErrorCode.GUID_ALREADY_ISSUED -> {
                                    bot.sendMsg(
                                        id,
                                        "Вам уже был сгенерирован GUID"
                                    )
                                }
                            }
                        }
                    }
                }
            }

            command("features") {
                bot.sendMsg(
                    message.chat.id,
                    "Возможности бота",
                    buttons.getButtons(ButtonsType.FUNCTIONAL_BUTTONS)
                )
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
                            bot.sendUserInfo(id, response.receive<User>())
                        }
                        HttpStatusCode.NotFound -> {
                            bot.sendMsg(
                                chatId = id,
                                text = "Вы не найдены в базе",
                                buttons = buttons.getButtons(ButtonsType.START_BUTTONS)
                            )
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
                                        val sessions = response.receive<List<Session>>()
                                            .filter { it.currentState != SessionState.FINISH }
                                        sessions.forEach {
                                            val description =
                                                if (it.description != null) "\nОписание ${it.description}" else ""
                                            bot.sendMsg(
                                                id,
                                                "Сессия ${it.id}$description\nБюджет ${it.budget}\n" +
                                                        "Дата окончания выбора подарков\n${getDateString(it.timestampToChoose)}\n" +
                                                        "Дата мероприятия\n${getDateString(it.eventTimestamp)}\n",
                                                buttons.getUserInfoAboutSessionButton(user.id, it.id)
                                            )
                                        }
                                    }
                                    else -> bot.sendMsg(
                                        id,
                                        "Что-то пошло не так"
                                    )
                                }
                            }
                        }
                        HttpStatusCode.NotFound -> {
                            bot.sendMsg(
                                id,
                                "Вы не найдены в базе",
                                buttons.getButtons(ButtonsType.START_BUTTONS)
                            )
                        }
                    }
                }
            }

            callbackQuery("userSessionInfo") {
                val userId = callbackQuery.data.split(" ")[1].toInt()
                val sessionId = callbackQuery.data.split(" ")[2].toInt()
                runBlocking {
                    val response =
                        client.post<HttpResponse>(config.server.url + config.server.getUserInfoAboutSessionRoute) {
                            method = HttpMethod.Post
                            contentType(ContentType.Application.Json)
                            body = UserSessionInfoRequest(userId, sessionId)
                        }
                    when(response.status){
                        HttpStatusCode.OK -> {
                            bot.sendUserSessionInfo(response.receive())
                        }
                    }
                }
            }
        }
    }

    fun start() = telegramBot.startWebhook()

    fun processUpdate(receiveBody: String) = telegramBot.processUpdate(receiveBody)

    fun remindToEndSession(session: Session, users: List<User>) {
        users.filter { it.telegramId != null }.forEach {
            telegramBot.sendMsg(
                it.telegramId!!,
                "${it.firstName}, сессия ${session.id} закончилась"
            )
        }
    }

    fun remindToChooseGift(userId: Long) {
        telegramBot.sendMsg(
            userId,
            "Вы еще не выбрали подарок в сессии ... . Поторопитесь!"
        )
    }

    fun remindToStartSession(session: Session, users: List<User>) {
        users.filter { it.telegramId != null }.forEach {
            telegramBot.sendMsg(
                it.telegramId!!,
                "${it.firstName}, сессия ${session.id} началась"
            )
        }
    }
}