package ru.rsreu.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.CallbackQuery
import ru.tinkoff.sanata.shared_models.model.Gift
import ru.tinkoff.sanata.shared_models.model.Session
import ru.tinkoff.sanata.shared_models.model.User
import ru.tinkoff.sanata.shared_models.request.CreateGiftRequest
import ru.tinkoff.sanata.shared_models.request.CreateSessionRequest
import ru.tinkoff.sanata.shared_models.response.UserInfoAboutSessionResponse
import java.time.LocalDateTime
import java.time.Month

class TelegramBotController {
    private val buttons = TgButtons()
    private val sessionCreatingList = mutableListOf<SessionCreating>()
    private val additionGiftList = mutableListOf<AdditionGift>()


    fun enterText(bot: Bot, id: Long, text: String) {
        enterCreatingSession(bot, id, text)
        enterAdditionGift(bot, id, text)
    }

    private fun enterCreatingSession(bot: Bot, id: Long, text: String) {
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
        sessionCreating.state.enterValue(bot, sessionCreating, text)
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

    fun successCreateSession(bot: Bot, callbackQuery: CallbackQuery) {
        val id = callbackQuery.from.id
        val sessionCreating = sessionCreatingList.find { it.telegramId == id }!!
        sessionCreatingList.remove(sessionCreating)
        bot.deleteLastMessage(callbackQuery)
        bot.sendMsg(
            id,
            "Сессия успешно создалась",
            buttons.getButtons(ButtonsType.SUCCESS_SESSION_CREATING_BUTTONS)
        )
    }

    fun cancelSessionCreating(bot: Bot, callbackQuery: CallbackQuery) {
        val id = callbackQuery.from.id
        bot.deleteLastMessage(callbackQuery)
        val sessionCreating = sessionCreatingList.find { it.telegramId == id }!!
        sessionCreatingList.remove(sessionCreating)
        bot.sendMsg(
            id,
            "Создание сессии отменено"
        )
    }

    fun startCreatingSession(bot: Bot, id: Long) {
        if (sessionCreatingList.find { it.telegramId == id } == null) {
            sessionCreatingList.add(SessionCreating(id, SessionCreatingState.ENTER_DESCRIPTION))
            bot.sendMsg(
                id,
                "Вы начала процесс создания сессии, чтобы закончить напишите \"отмена\". Введите описание сессии"
            )
        }
    }

    fun startCreatingGift(bot: Bot, id: Long, userId: Int, sessionId: Int) {
        if (additionGiftList.find { it.telegramId == id } == null) {
            additionGiftList.add(AdditionGift(id, userId, sessionId))
            bot.sendMsg(
                id,
                "Чтобы добавить подарок введите название и описание подарка через пробел. Введите \"отмена\",чтобы отменить процесс"
            )
        }
    }

    private fun enterAdditionGift(bot: Bot, id: Long, text: String) {
        val additionGift = additionGiftList.find { it.telegramId == id }
        if (additionGift != null) {
            if (text == "отмена") {
                additionGiftList.remove(additionGift)
                bot.sendMsg(
                    id,
                    "Процесс добавления поадрка отменен"
                )
            } else {
                additionGiftProcess(bot, id, text)
            }
        }
    }

    private fun additionGiftProcess(bot: Bot, id: Long, text: String) {
        val additionGift = additionGiftList.find { it.telegramId == id }!!
        val strings = text.split(" ")
        if (strings.size == 1) {
            additionGift.giftName = text
        } else {
            additionGift.giftName = strings[0]
            additionGift.giftDescription = strings[1]
        }
        bot.sendMsg(
            id,
            "Вы уверены что хотите добавить подарок?",
            buttons.getButtons(ButtonsType.ADDITION_GIFT_BUTTONS)
        )
    }

    fun getAddingGiftRequest(id: Long): CreateGiftRequest {
        val additionGift = additionGiftList.find { it.telegramId == id }!!
        return CreateGiftRequest(
            additionGift.userId,
            additionGift.sessionId,
            additionGift.giftName!!,
            additionGift.giftDescription
        )
    }

    fun successAddGift(bot: Bot, callbackQuery: CallbackQuery, gift: Gift, userId: Int, sessionId: Int) {
        val id = callbackQuery.from.id
        val additionGift = additionGiftList.find { it.telegramId == id }
        additionGiftList.remove(additionGift)
        bot.deleteLastMessage(callbackQuery)
        bot.sendMsg(
            id,
            "Вы успешно добавили подарок ${getGiftString(gift)}",
            buttons.getSessionAfterAddGiftButton(userId, sessionId)
        )
    }

    fun cancelAdditionGift(bot: Bot, callbackQuery: CallbackQuery) {
        bot.deleteLastMessage(callbackQuery)
        val id = callbackQuery.from.id
        val additionGift = additionGiftList.find { it.telegramId == id }
        additionGiftList.remove(additionGift)
        bot.sendMsg(
            id,
            "Создание подарка отменено"
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
        val user = info.user
        val usersString = formUsersString(info.users)
        val giftsString = formUserGifts(info.userGifts, "Список выбранных подарков:\n", "Вы не выбрали ни 1 подарка\n")
        val giftGivingInfo = formGiftGivingInfo(info.giftReceivingUser, info.receivingUserGifts)
        bot.sendMsg(
            info.user.telegramId!!,
            formMainSessionInfo(session) + usersString + giftsString + giftGivingInfo,
            buttons.getAdditionalGiftButton(user.id, session.id)
        )
    }

    fun sendMessageAboutSuccessLeave(bot: Bot, id: Long, sessionId: Int) =
        bot.sendMsg(id, "Вы успешно покинулу сессию $sessionId")

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