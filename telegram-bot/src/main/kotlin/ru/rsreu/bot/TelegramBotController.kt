package ru.rsreu.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import ru.tinkoff.sanata.shared_models.model.Gift
import ru.tinkoff.sanata.shared_models.model.Session
import ru.tinkoff.sanata.shared_models.model.User
import ru.tinkoff.sanata.shared_models.request.CreateSessionRequest
import ru.tinkoff.sanata.shared_models.response.UserInfoAboutSessionResponse
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter

class TelegramBotController {
    private val buttons = TgButtons()
    private val sessionCreatingList = mutableListOf<SessionCreating>()

    fun startCreatingSession(bot: Bot, id: Long) {
        if (sessionCreatingList.find { it.telegramId == id } == null) {
            sessionCreatingList.add(SessionCreating(id, SessionCreatingState.ENTER_DESCRIPTION))
        }
        bot.sendMsg(
            id,
            "Вы начала процесс создания сессии, чтобы закончить напишите \"отмена\". Введите описание сессии"
        )
    }

    fun enterText(bot: Bot, id: Long, text: String) {
        val sessionCreating = sessionCreatingList.find { it.telegramId == id }
        if (sessionCreating != null) {
            if (text == "отмена") {
                sessionCreatingList.remove(sessionCreating)
                bot.sendMsg(
                    id,
                    "Процесс создания сессии отменен"
                )
            } else {
                creatingProcess(bot, id, text)
            }
        }
    }

    private fun creatingProcess(bot: Bot, id: Long, text: String) {
        val sessionCreating = sessionCreatingList.find { it.telegramId == id }!!
        // полиморфизм
        when (sessionCreating.state) {
            SessionCreatingState.ENTER_DESCRIPTION -> {
                successEnterDescription(bot, sessionCreating, text)
            }
            SessionCreatingState.ENTER_BUDGET -> {
                enterBudget(bot, sessionCreating, text)
            }
            SessionCreatingState.ENTER_MIN_PLAYERS -> {
                enterMinPlayers(bot, sessionCreating, text)
            }
            SessionCreatingState.ENTER_EVENT_TIMESTAMP -> {
                enterEventTimestamp(bot, sessionCreating, text)
            }
            SessionCreatingState.ENTER_TIMESTAMP_TO_CHOOSE -> {
                enterTimestampToChoose(bot, sessionCreating, text)
            }
        }
    }

    private fun enterTimestampToChoose(bot: Bot, sessionCreating: SessionCreating, timestampToChooseString: String) {
        if (checkTimestampToChoose(timestampToChooseString)) {
            successEnterTimestampToChoose(bot, sessionCreating, timestampToChooseString)
        } else {
            failEnterTimestampToChoose(bot, sessionCreating)
        }
    }

    private fun checkTimestampToChoose(timestampToChooseString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return try {
            LocalDateTime.parse(timestampToChooseString, formatter)
            true
        } catch (exception: Exception) {
            false
        }
    }

    private fun successEnterTimestampToChoose(
        bot: Bot,
        sessionCreating: SessionCreating,
        timestampToChooseString: String
    ) {
        sessionCreating.timestampToChoose = timestampToChooseString
        bot.sendMsg(
            sessionCreating.telegramId,
            sessionCreating.toString(),
            buttons.getButtons(ButtonsType.SESSION_CREATING_BUTTONS)
        )
    }

    private fun failEnterTimestampToChoose(bot: Bot, sessionCreating: SessionCreating) {
        bot.sendMsg(
            sessionCreating.telegramId,
            "Дата должна соответствовать формату yyyy-MM-dd HH:mm"
        )
    }

    private fun successEnterDescription(bot: Bot, sessionCreating: SessionCreating, text: String) {
        sessionCreating.description = text
        sessionCreating.state = SessionCreatingState.ENTER_BUDGET
        bot.sendMsg(
            sessionCreating.telegramId,
            "Введите бюджет"
        )
    }

    private fun enterEventTimestamp(bot: Bot, sessionCreating: SessionCreating, eventTimestampString: String) {
        if (checkEventTimestamp(eventTimestampString)) {
            successEnterEventTimestamp(bot, sessionCreating, eventTimestampString)
        } else {
            failEnterEventTimestamp(bot, sessionCreating)
        }
    }

    private fun checkEventTimestamp(eventTimestampString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return try {
            LocalDateTime.parse(eventTimestampString, formatter)
            true
        } catch (exception: Exception) {
            false
        }
    }

    private fun successEnterEventTimestamp(bot: Bot, sessionCreating: SessionCreating, eventTimestampString: String) {
        sessionCreating.eventTimestamp = eventTimestampString
        sessionCreating.state = SessionCreatingState.ENTER_TIMESTAMP_TO_CHOOSE
        bot.sendMsg(
            sessionCreating.telegramId,
            "Введите дату окончания выбора подарков"
        )
    }

    private fun failEnterEventTimestamp(bot: Bot, sessionCreating: SessionCreating) {
        bot.sendMsg(
            sessionCreating.telegramId,
            "Дата должна соответствовать формату yyyy-MM-dd HH:mm"
        )
    }

    private fun enterBudget(bot: Bot, sessionCreating: SessionCreating, budgetString: String) {
        if (checkBudget(budgetString)) {
            successEnterBudget(bot, sessionCreating, budgetString.toInt())
        } else {
            failEnterBudget(bot, sessionCreating)
        }
    }

    private fun checkBudget(budgetString: String): Boolean {
        val budget = budgetString.toIntOrNull() ?: return false
        if ((budget < 100) or (budget > 10000)) {
            return false
        }
        return true
    }

    private fun successEnterBudget(bot: Bot, sessionCreating: SessionCreating, budget: Int) {
        sessionCreating.budget = budget
        sessionCreating.state = SessionCreatingState.ENTER_MIN_PLAYERS
        bot.sendMsg(
            sessionCreating.telegramId,
            "Введите минимальное количество игроков"
        )
    }

    private fun failEnterBudget(bot: Bot, sessionCreating: SessionCreating) {
        bot.sendMsg(
            sessionCreating.telegramId,
            "Бюджет должен быть натуральным числом в диапазоне [100,10000]"
        )
    }


    fun getCreateSessionRequest(id: Long): CreateSessionRequest {
        val sessionCreating = sessionCreatingList.find { it.telegramId == id }!!
        return CreateSessionRequest(
            sessionCreating.description,
            null,
            id,
            sessionCreating.budget!!,
            sessionCreating.eventTimestamp!!,
            sessionCreating.timestampToChoose!!,
            sessionCreating.minPlayersQuantity
        )
    }

    private fun enterMinPlayers(bot: Bot, sessionCreating: SessionCreating, minPlayersString: String) {
        if (checkMinPlayers(minPlayersString)) {
            successEnterMinPlayers(bot, sessionCreating, minPlayersString.toInt())
        } else {
            failEnterMinPlayers(bot, sessionCreating)
        }
    }

    private fun checkMinPlayers(minPlayersString: String): Boolean {
        val minPlayers = minPlayersString.toIntOrNull() ?: return false
        if ((minPlayers < 3) or (minPlayers > 10)) {
            return false
        }
        return true
    }

    private fun successEnterMinPlayers(bot: Bot, sessionCreating: SessionCreating, minPlayers: Int) {
        sessionCreating.minPlayersQuantity = minPlayers
        sessionCreating.state = SessionCreatingState.ENTER_EVENT_TIMESTAMP
        bot.sendMsg(
            sessionCreating.telegramId,
            "Введите дату мероприятия"
        )
    }

    private fun failEnterMinPlayers(bot: Bot, sessionCreating: SessionCreating) {
        bot.sendMsg(
            sessionCreating.telegramId,
            "Количество участников должно быть числом большем 2"
        )
    }

    fun successCreateSession(bot: Bot, id: Long, callbackQuery: CallbackQuery) {
        bot.deleteLastMessage(callbackQuery)
        bot.sendMsg(
            id,
            "Сессия успешно создалась",
            buttons.getButtons(ButtonsType.SUCCESS_SESSION_CREATING_BUTTONS)
        )
    }

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
        if (sessions.isEmpty()) {
            bot.sendMsg(
                id,
                "У вас нет активных сессий"
            )
        }
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