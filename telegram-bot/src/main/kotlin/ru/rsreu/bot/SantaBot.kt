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
import ru.tinkoff.sanata.shared_models.request.CreateGuidRequest
import ru.tinkoff.sanata.shared_models.request.CreateSessionRequest

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
                runBlocking {
                    val response = client.post<HttpResponse>(config.server.url + config.server.guidCreateRoute) {
                        method = HttpMethod.Post
                        contentType(ContentType.Application.Json)
                        body = CreateGuidRequest(callbackQuery.from.id)
                    }
                    when (response.status) {
                        HttpStatusCode.Created -> {
                            bot.deleteLastMessage(callbackQuery)
                            bot.sendSomeMsg(
                                callbackQuery.from.id,
                                "Вы успешно зарегистрировались, ваш GUID:",
                                response.receive(),
                                "Укажите его в своем личном кабинете, чтобы привязать аккаунт ... ."
                            )
                            bot.sendMsg(
                                chatId = callbackQuery.from.id,
                                text = "Возможности бота:",
                                buttons = buttons.getButtons(ButtonsType.FUNCTIONAL_BUTTONS)
                            )
                        }
                    }
                }
            }

            callbackQuery("create") {
                runBlocking {
                    val id = callbackQuery.from.id
                    val response = client.post<HttpResponse>(config.server.url + config.server.createSessionRoute) {
                        method = HttpMethod.Post
                        contentType(ContentType.Application.Json)
                        body = CreateSessionRequest(
                            "ОПИСАНИЕ",
                            null,
                            id,
                            10000,
                            "2021-12-31 12:00",
                            "2021-12-20 12:00",
                            5
                        )
                    }
                    when (response.status) {
                        HttpStatusCode.Created -> {
                            bot.sendMsg(
                                chatId = callbackQuery.from.id,
                                text = "Успешно создалось"
                            )
                        }
                    }
                }
            }

            callbackQuery("sessions") {
                runBlocking {
                    val id = callbackQuery.from.id
                    val response =
                        client.get<HttpResponse>(config.server.url + config.server.getSessionRoute + id.toString()) {
                            method = HttpMethod.Get
                            contentType(ContentType.Application.Json)
                        }
                    when (response.status) {
                        HttpStatusCode.OK -> {
                            val sessions = response.receive<List<Session?>>()
                            sessions.forEach {
                                bot.sendMsg(
                                    chatId = callbackQuery.from.id,
                                    text = it.toString(),
                                    buttons = buttons.getButtons(ButtonsType.LOBBY_BUTTONS)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun start() = telegramBot.startWebhook()

    fun processUpdate(receiveBody: String) = telegramBot.processUpdate(receiveBody)

    private fun getGUID() = "{6F9619FF-8B86-D011-B42D-00CF4FC964FF}"

    fun sendMe(text: String) {
        telegramBot.sendMsg(383852636, text)
    }

    fun remindToEndSession(userId: Long) {
        telegramBot.sendMsg(
            userId,
            "Сессия скоро начнется, успейте добавить/изменить подарок до ..."
        )
    }

    fun remindToChooseGift(userId: Long) {
        telegramBot.sendMsg(
            userId,
            "Вы еще не выбрали подарок в сессии ... . Поторопитесь!"
        )
    }

    fun remindToStartSession(userId: Long) {
        telegramBot.sendMsg(
            userId,
            "Сессия ... началась. Вы дарите подарок ..."
        )
    }
}