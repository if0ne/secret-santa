package ru.rsreu.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ReplyMarkup
import ru.tinkoff.sanata.shared_models.model.User
import ru.tinkoff.sanata.shared_models.response.UserInfoAboutSessionResponse
import java.lang.StringBuilder
import java.time.LocalDateTime
import java.time.Month

fun Bot.sendMsg(chatId: Long, text: String, buttons: ReplyMarkup? = null) =
    this.sendMessage(
        chatId = ChatId.fromId(chatId),
        text = text,
        replyMarkup = buttons
    )

fun Bot.sendSomeMsg(chatId: Long, vararg texts: String) {
    texts.forEach {
        sendMsg(chatId, it)
    }
}

fun Bot.sendPhoto(chatId: Long, photo: String) {
    this.sendPhoto(ChatId.fromId(chatId), photo)
}

fun Bot.deleteLastMessage(callbackQuery: CallbackQuery) {
    this.deleteMessage(
        chatId = ChatId.fromId(callbackQuery.from.id),
        messageId = callbackQuery.message!!.messageId
    )
}

