package ru.rsreu.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import ru.tinkoff.sanata.shared_models.model.Gift
import ru.tinkoff.sanata.shared_models.model.Session
import ru.tinkoff.sanata.shared_models.model.User
import ru.tinkoff.sanata.shared_models.response.UserInfoAboutSessionResponse
import java.time.LocalDateTime
import java.time.Month

class TelegramBotController {
    private val buttons = TgButtons()

    fun sendWelcomeMessage(bot: Bot, chatId: Long) {
        bot.sendMsg(
            chatId = chatId,
            text = "Добро пожаловать в Тайного Санту",
            buttons = buttons.getButtons(ButtonsType.START_BUTTONS)
        )
    }

    fun sendGuid(bot: Bot, callbackQuery: CallbackQuery, guid: String) {
        val id = callbackQuery.from.id
        bot.deleteLastMessage(callbackQuery)
        bot.sendSomeMsg(
            id,
            "Ваш GUID:",
            guid,
            "Укажите его в своем личном кабинете, чтобы привязать аккаунт ... ."
        )
        bot.sendMsg(
            id,
            "Возможности бота:",
            buttons.getButtons(ButtonsType.FUNCTIONAL_BUTTONS)
        )
    }

    fun sendMessageAboutLinkedAccount(bot: Bot, chatId: Long) {
        bot.sendMsg(
            chatId,
            "Ваш аккаунт уже привязан. Возможности бота: ",
            buttons.getButtons(ButtonsType.FUNCTIONAL_BUTTONS)
        )
    }

    fun sendMessageAboutAlreadyIssued(bot: Bot, chatId: Long) {
        bot.sendMsg(
            chatId,
            "Вам уже был сгенерирован GUID"
        )
    }

    fun sendFeatures(bot: Bot, chatId: Long) = bot.sendMsg(
        chatId,
        "Возможности бота",
        buttons.getButtons(ButtonsType.FUNCTIONAL_BUTTONS)
    )

    fun sendUserInfo(bot: Bot, chatId: Long, user: User) {
        val fullName = getFullName(user)
        bot.sendMsg(
            chatId,
            "Вы привязаны к аккаунту:\n$fullName"
        )
        val photo = user.avatarUrl
        if (photo != null) {
            bot.sendPhoto(chatId, photo)
        }
    }

    private fun getFullName(user: User) =
        if (user.middleName == null) "${user.firstName} ${user.lastName}" else "${user.firstName} ${user.middleName} ${user.lastName}"

    fun sendMessageAboutNotLinkedAccount(bot: Bot, chatId: Long) = bot.sendMsg(
        chatId,
        "Вы не привязаны к аккаунту",
        buttons.getButtons(ButtonsType.START_BUTTONS)
    )

    fun notifyAboutStartSession(bot: Bot, user: User, session: Session) = bot.sendMsg(
        user.telegramId!!,
        "${user.firstName}, выбор подарков в сессии ${session.id} закончился",
        buttons.getUserInfoAboutSessionButton(user.id, session.id)
    )

    fun notifyAboutEndSession(bot: Bot, user: User, session: Session) = bot.sendMsg(
        user.telegramId!!,
        "${user.firstName}, сессия ${session.id} закончилась"
    )

    fun sendSessions(bot: Bot, user: User, sessions: List<Session>) {
        val id = user.telegramId!!
        sessions.forEach {
            bot.sendMsg(
                id,
                formMainSessionInfo(it),
                buttons.getUserInfoAboutSessionButton(user.id, it.id)
            )
        }
    }

    fun sendUserSessionInfo(bot: Bot, info: UserInfoAboutSessionResponse) {
        val session = info.session
        val usersString = formUsersString(info.users)
        val giftsString = formUserGifts(info.userGifts, "Список выбранных подарков:\n", "Вы не выбрали ни 1 подарка\n")
        val giftGivingInfo = formGiftGivingInfo(info.giftReceivingUser, info.receivingUserGifts)
        bot.sendMsg(
            info.user.telegramId!!,
            formMainSessionInfo(session) + usersString + giftsString + giftGivingInfo
        )
    }

    private fun formMainSessionInfo(session: Session): String = "Сессия ${session.id}" +
            formDescription(session.description) +
            "\nБюджет ${session.budget}\n" +
            "Дата окончания выбора подарков\n${getDateString(session.timestampToChoose)}\n" +
            "Дата мероприятия\n${getDateString(session.eventTimestamp)}\n"

    private fun formDescription(description: String?) = if (description != null) "\nОписание: $description" else ""

    private fun formGiftGivingInfo(giftReceivingUser: User?, gifts: List<Gift>) =
        if (giftReceivingUser != null) "Вы дарите подарок пользователю ${getFullName(giftReceivingUser)}\n" +
                formReceivingUserGiftsString(gifts) else ""

    private fun formReceivingUserGiftsString(gifts: List<Gift>): String =
        formUserGifts(gifts, "Он хочет получить:\n", "К сожалению, он ничего не выбрал\n")

    private fun formUserGifts(gifts: List<Gift>, preString: String, emptyString: String): String {
        if (gifts.isEmpty()) {
            return emptyString
        }
        val result = StringBuilder(preString)
        gifts.forEach {
            result.append(getGiftString(it)).append("\n")
        }
        return result.toString()
    }

    private fun getGiftString(gift: Gift) =
        if (gift.description == null) gift.name else "${gift.name}(${gift.description})"

    private fun getDateString(date: LocalDateTime): String =
        "${date.dayOfMonth} ${getRussianMonthString(date.month)} ${date.year} ${getTwoDigitFormat(date.hour)}:${
            getTwoDigitFormat(
                date.minute
            )
        }"

    private fun getTwoDigitFormat(number: Int): String = if (number > 9) number.toString() else "0$number"

    private fun getRussianMonthString(month: Month): String {
        return when (month) {
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

    private fun formUsersString(users: List<User>): String {
        val output = StringBuilder("Участники:\n")
        users.forEach {
            output.append(getFullName(it)).append("\n")
        }
        return output.toString()
    }

}