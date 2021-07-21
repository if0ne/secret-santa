package ru.rsreu.bot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.webhook
import ru.rsreu.TelegramConfig

//383852636

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
                bot.sendSomeMsg(
                    callbackQuery.from.id,
                    "Вы успешно зарегистрировались, ваш GUID:",
                    guid,
                    "Укажите его в своем личном кабинете, чтобы привязать аккаунт ... ."
                )
                bot.sendMsg(
                    chatId = callbackQuery.from.id,
                    text = "Возможности бота:",
                    buttons = buttons.getButtons(ButtonsType.FUNCTIONAL_BUTTONS)
                )
            }

            callbackQuery("create") { }

            callbackQuery("sessions") {
                bot.sendMsg(
                    chatId = callbackQuery.from.id,
                    text = "Список ваших сессий:",
                    buttons = buttons.getButtons(ButtonsType.LOBBY_BUTTONS)
                )
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