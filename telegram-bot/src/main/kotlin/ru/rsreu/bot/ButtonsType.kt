package ru.rsreu.bot

import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton

enum class ButtonsType {
    START_BUTTONS {
        override fun getButtons() = InlineKeyboardMarkup.createSingleRowKeyboard(
            InlineKeyboardButton.CallbackData("Привязать аккаунт", "register")
        )
    },
    FUNCTIONAL_BUTTONS {
        override fun getButtons() = InlineKeyboardMarkup.createSingleRowKeyboard(
            InlineKeyboardButton.CallbackData("Информация об аккаунте", "userInfo"),
            InlineKeyboardButton.CallbackData("Список моих сессий", "sessions")
        )
    };

    abstract fun getButtons(): InlineKeyboardMarkup
}