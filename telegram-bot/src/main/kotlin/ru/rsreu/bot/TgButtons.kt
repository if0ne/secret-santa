package ru.rsreu.bot

import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton

class TgButtons {
    fun getButtons(type: ButtonsType) = type.getButtons()

    fun getUserInfoAboutSessionButton(userId: Int, sessionId: Int) = InlineKeyboardMarkup.createSingleRowKeyboard(
        InlineKeyboardButton.CallbackData("Больше информации", "userSessionInfo $userId $sessionId")
    )
}