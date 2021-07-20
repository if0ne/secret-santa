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
                bot.sendMsg(message.chat.id, text = "hello", buttons.getButtons(ButtonsType.START_BUTTONS))
            }

            callbackQuery("create") {
                bot.deleteLastMessage(callbackQuery)
            }

            callbackQuery("join") {
                bot.sendMsg(callbackQuery.from.id, "AGAG")
            }
        }
    }

    fun start() = telegramBot.startWebhook()

    fun processUpdate(receiveBody: String) = telegramBot.processUpdate(receiveBody)
}