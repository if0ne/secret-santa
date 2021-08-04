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
            InlineKeyboardButton.CallbackData("Список моих сессий", "sessions"),
            InlineKeyboardButton.CallbackData("Создать сессию", "create")
        )
    },
    SUCCESS_SESSION_CREATING_BUTTONS{
        override fun getButtons() = InlineKeyboardMarkup.createSingleRowKeyboard(
            InlineKeyboardButton.CallbackData("Список моих сессий", "sessions")
        )
    },
    SESSION_CREATING_BUTTONS{
        override fun getButtons() = InlineKeyboardMarkup.createSingleRowKeyboard(
        InlineKeyboardButton.CallbackData("Создать", "confirmCreating"),
        InlineKeyboardButton.CallbackData("Изменить", "changeCreating"),
        InlineKeyboardButton.CallbackData("Отмена", "cancelCreating")
        )
    };

    abstract fun getButtons(): InlineKeyboardMarkup
}