package ru.rsreu.bot

import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton

class TgButtons {
    fun getButtons(type: ButtonsType) = type.getButtons()

    fun getUserInfoAboutSessionButtons(userId: Int, sessionId: Int) = InlineKeyboardMarkup.createSingleRowKeyboard(
        InlineKeyboardButton.CallbackData("Больше информации", "userSessionInfo $userId $sessionId"),
        InlineKeyboardButton.CallbackData("Выйти", "leave $userId $sessionId")
    )

    fun getSessionButtons(userId: Int, sessionId: Int) = InlineKeyboardMarkup.createSingleRowKeyboard(
        InlineKeyboardButton.CallbackData("Добавить подарок", "addGift $userId $sessionId"),
        InlineKeyboardButton.CallbackData("Выйти", "leave $userId $sessionId")
    )

    fun getSessionAfterAddGiftButton(userId: Int, sessionId: Int) = InlineKeyboardMarkup.createSingleRowKeyboard(
        InlineKeyboardButton.CallbackData("Информация о сессии", "userSessionInfo $userId $sessionId")
    )
}
