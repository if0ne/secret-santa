package ru.rsreu.bot

import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton

enum class ButtonsType {
    START_BUTTONS {
        override fun getButtons() = InlineKeyboardMarkup.createSingleRowKeyboard(
            InlineKeyboardButton.CallbackData("Регистрация", "register")
        )
    },
    FUNCTIONAL_BUTTONS {
        override fun getButtons() = InlineKeyboardMarkup.createSingleRowKeyboard(
            InlineKeyboardButton.CallbackData("Создать лобби", "create"),
            InlineKeyboardButton.CallbackData("Список моих лобби", "lobbies"),
        )
    };

    abstract fun getButtons(): InlineKeyboardMarkup
}