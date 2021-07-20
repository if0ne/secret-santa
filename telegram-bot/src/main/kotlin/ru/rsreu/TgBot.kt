package ru.rsreu

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.webhook

class TgBot(config: TelegramConfig) {

    private val bot = bot {
        token = config.token
        webhook {
            url = config.webhookUrl
        }
        dispatch {
            command("start") {
                bot.sendMessage(
                    chatId = ChatId.fromId(message.chat.id),
                    text = "hello"
                )
            }
        }
    }

    fun start() = bot.startWebhook()

    fun processUpdate(receiveBody: String) = bot.processUpdate(receiveBody)


}