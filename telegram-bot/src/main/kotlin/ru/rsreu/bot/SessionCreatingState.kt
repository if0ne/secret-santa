package ru.rsreu.bot

import com.github.kotlintelegrambot.Bot
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class SessionCreatingState {
    ENTER_DESCRIPTION {
        override fun enterValue(bot: Bot, sessionCreating: SessionCreating, text: String) =
            successEnterDescription(bot, sessionCreating, text)

        private fun successEnterDescription(bot: Bot, sessionCreating: SessionCreating, text: String) {
            sessionCreating.description = text
            sessionCreating.state = ENTER_BUDGET
            bot.sendMsg(
                sessionCreating.telegramId,
                "Введите бюджет"
            )
        }
    },
    ENTER_BUDGET {
        override fun enterValue(bot: Bot, sessionCreating: SessionCreating, text: String) {
            if (checkBudget(text)) {
                successEnterBudget(bot, sessionCreating, text.toInt())
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
            sessionCreating.state = ENTER_MIN_PLAYERS
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
    },
    ENTER_MIN_PLAYERS {
        override fun enterValue(bot: Bot, sessionCreating: SessionCreating, text: String) {
            if (checkMinPlayers(text)) {
                successEnterMinPlayers(bot, sessionCreating, text.toInt())
            } else {
                failEnterMinPlayers(bot, sessionCreating)
            }
        }

        private fun checkMinPlayers(minPlayersString: String): Boolean {
            val minPlayers = minPlayersString.toIntOrNull() ?: return false
            if ((minPlayers < 3) or (minPlayers > 11)) {
                return false
            }
            return true
        }

        private fun successEnterMinPlayers(bot: Bot, sessionCreating: SessionCreating, minPlayers: Int) {
            sessionCreating.minPlayersQuantity = minPlayers
            sessionCreating.state = ENTER_EVENT_TIMESTAMP
            bot.sendMsg(
                sessionCreating.telegramId,
                "Введите дату мероприятия"
            )
        }

        private fun failEnterMinPlayers(bot: Bot, sessionCreating: SessionCreating) {
            bot.sendMsg(
                sessionCreating.telegramId,
                "Количество участников должно быть числом от 3 до 10"
            )
        }
    },
    ENTER_EVENT_TIMESTAMP {
        override fun enterValue(bot: Bot, sessionCreating: SessionCreating, text: String) {
            if (checkEventTimestamp(text)) {
                successEnterEventTimestamp(bot, sessionCreating, text)
            } else {
                failEnterEventTimestamp(bot, sessionCreating)
            }
        }

        private fun checkEventTimestamp(eventTimestampString: String): Boolean {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            return try {
                val eventTimestamp = LocalDateTime.parse(eventTimestampString, formatter)
                if (eventTimestamp < LocalDateTime.now()) {
                    return false
                }
                true
            } catch (exception: Exception) {
                false
            }
        }

        private fun successEnterEventTimestamp(
            bot: Bot,
            sessionCreating: SessionCreating,
            eventTimestampString: String
        ) {
            sessionCreating.eventTimestamp = eventTimestampString
            sessionCreating.state = ENTER_TIMESTAMP_TO_CHOOSE
            bot.sendMsg(
                sessionCreating.telegramId,
                "Введите дату окончания выбора подарков"
            )
        }

        private fun failEnterEventTimestamp(bot: Bot, sessionCreating: SessionCreating) {
            bot.sendMsg(
                sessionCreating.telegramId,
                "Дата должна соответствовать формату yyyy-MM-dd HH:mm. Нельзя вводить текущую дата и раньше."
            )
        }
    },
    ENTER_TIMESTAMP_TO_CHOOSE {
        override fun enterValue(bot: Bot, sessionCreating: SessionCreating, text: String) {
            if (checkTimestampToChoose(text, sessionCreating)) {
                successEnterTimestampToChoose(bot, sessionCreating, text)
            } else {
                failEnterTimestampToChoose(bot, sessionCreating)
            }
        }

        private fun checkTimestampToChoose(timestampToChooseString: String, sessionCreating: SessionCreating): Boolean {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            return try {
                val timestampToChoose = LocalDateTime.parse(timestampToChooseString, formatter)
                if (timestampToChoose < LocalDateTime.now()) {
                    return false
                }
                if (timestampToChoose > LocalDateTime.parse(sessionCreating.eventTimestamp, formatter)) {
                    return false
                }
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
                "Вы уверены, что хотите сдать сессию с параметрами:\n" +
                        "Описание: ${sessionCreating.description}\n" +
                        "Бюджет: ${sessionCreating.budget}\n" +
                        "Минимальное количетсво участников: ${sessionCreating.minPlayersQuantity}\n" +
                        "Дата события: ${sessionCreating.eventTimestamp}\n" +
                        "Дата окончания выбора подарков: ${sessionCreating.timestampToChoose}",
                TgButtons().getButtons(ButtonsType.SESSION_CREATING_BUTTONS)
            )
        }

        private fun failEnterTimestampToChoose(bot: Bot, sessionCreating: SessionCreating) {
            bot.sendMsg(
                sessionCreating.telegramId,
                "Дата должна соответствовать формату yyyy-MM-dd HH:mm. Нельзя вводить текущую дата и раньше. " +
                        "Дата конца выбора подарком должна быть раньше даты события"
            )
        }
    };

    abstract fun enterValue(bot: Bot, sessionCreating: SessionCreating, text: String)
}