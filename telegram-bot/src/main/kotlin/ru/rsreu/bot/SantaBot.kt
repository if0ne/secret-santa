package ru.rsreu.bot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.webhook
import ru.rsreu.TelegramConfig
import ru.rsreu.deleteLastMessage
import ru.rsreu.sendMsg

class SantaBot(config: TelegramConfig) {

    private val buttons = TgButtons()

    private val telegramBot = bot {
        token = config.token
        webhook {
            url = config.webhookUrl
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
                val guid = getGUID()

                bot.deleteLastMessage(callbackQuery)
                bot.sendMsg(
                    chatId = callbackQuery.from.id,
                    text = "Вы успешно зарегистрировались, ваш GUID:"
                )
                bot.sendMsg(
                    chatId = callbackQuery.from.id,
                    text = guid
                )
                bot.sendMsg(
                    chatId = callbackQuery.from.id,
                    text = "Укажите его в своем личном кабинете, чтобы привязать аккаунт ... . Возможности бота:",
                    buttons = buttons.getButtons(ButtonsType.FUNCTIONAL_BUTTONS)
                )
            }

            callbackQuery("create") { }

            callbackQuery("lobbies") { }
        }
    }

    fun start() = telegramBot.startWebhook()

    fun processUpdate(receiveBody: String) = telegramBot.processUpdate(receiveBody)

    private fun getGUID() = "{6F9619FF-8B86-D011-B42D-00CF4FC964FF}"
}