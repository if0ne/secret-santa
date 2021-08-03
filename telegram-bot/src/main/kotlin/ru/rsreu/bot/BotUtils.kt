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

fun Bot.sendUserInfo(chatId: Long, user: User) {
    val fullName = getFullName(user)
    this.sendMsg(
        chatId,
        "Вы привязаны к аккаунту:\n$fullName"
    )
    val photo = user.avatarUrl
    if (photo != null) {
        this.sendPhoto(chatId, photo)
    }
}

fun Bot.sendUserSessionInfo(info: UserInfoAboutSessionResponse) {
    val session = info.session
    val users = getUsersString(info.users)
    val description =
        if (session.description != null) "\nОписание ${session.description}" else ""
    val giftReceivingUser = info.giftReceivingUser
    val giftGivingInfo = if(giftReceivingUser != null) "Вы дарите подарок пользователю ${getFullName(giftReceivingUser)}\n" +
            "Он выбрал ${info.receivingUserGifts}" else ""
    this.sendMsg(
        info.user.telegramId!!,
        "Сессия ${session.id}$description\nБюджет: ${session.budget}\n" +
                "Дата окончания выбора подарков:\n${getDateString(session.timestampToChoose)}\n" +
                "Дата мероприятия:\n${getDateString(session.eventTimestamp)}\n" +
                "Участники:\n$users" + giftGivingInfo
    )
}

fun getFullName(user: User) =
    if (user.middleName == null) "${user.firstName} ${user.lastName}" else "${user.firstName} ${user.middleName} ${user.lastName}"

fun getDateString(date: LocalDateTime): String =
    "${date.dayOfMonth} ${getRussianMonthString(date.month)} ${date.year} ${getTwoDigitFormat(date.hour)}:${getTwoDigitFormat(date.minute)}"

fun getTwoDigitFormat(number: Int): String = if (number > 9) number.toString() else "0$number"

fun getRussianMonthString(month: Month): String{
    return when(month){
        Month.JANUARY -> "Января"
        Month.FEBRUARY -> "Февраля"
        Month.MARCH -> "Марта"
        Month.APRIL -> "Апреля"
        Month.MAY -> "Мая"
        Month.JUNE -> "Июня"
        Month.JULY -> "Июля"
        Month.AUGUST -> "Августа"
        Month.SEPTEMBER -> "Сентября"
        Month.OCTOBER -> "Октября"
        Month.NOVEMBER -> "Ноября"
        Month.DECEMBER -> "Декабря"
    }
}

fun getUsersString(users: List<User>): String{
    val output = StringBuilder()
    users.forEach {
        output.append(getFullName(it)).append("\n")
    }
    return output.toString()
}


