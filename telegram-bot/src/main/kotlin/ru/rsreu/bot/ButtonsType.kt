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
        override fun getButtons() = InlineKeyboardMarkup.create(
            listOf(
                listOf(
                    InlineKeyboardButton.CallbackData("Информация об аккаунте", "userInfo")
                ),
                listOf(InlineKeyboardButton.CallbackData("Список моих сессий", "sessions")),
                listOf(InlineKeyboardButton.CallbackData("Создать сессию", "create")),
                listOf(InlineKeyboardButton.CallbackData("Войти в сессию", "join"))
            )
        )
    },
    SUCCESS_SESSION_CREATING_BUTTONS {
        override fun getButtons() = InlineKeyboardMarkup.createSingleRowKeyboard(
            InlineKeyboardButton.CallbackData("Список моих сессий", "sessions")
        )
    },
    SESSION_CREATING_BUTTONS {
        override fun getButtons() = InlineKeyboardMarkup.createSingleRowKeyboard(
            InlineKeyboardButton.CallbackData("Подтвердить", "confirmCreating"),
            InlineKeyboardButton.CallbackData("Отмена", "cancelCreating")
        )
    },
    ADDITION_GIFT_BUTTONS {
        override fun getButtons() = InlineKeyboardMarkup.createSingleRowKeyboard(
            InlineKeyboardButton.CallbackData("Добавить", "confirmAddition"),
            InlineKeyboardButton.CallbackData("Отмена", "cancelAddition")
        )
    },
    JOIN_SESSION_BUTTONS {
        override fun getButtons() = InlineKeyboardMarkup.createSingleRowKeyboard(
        InlineKeyboardButton.CallbackData("Войти", "confirmJoin"),
        InlineKeyboardButton.CallbackData("Отмена", "cancelJoin")
        )
    };


    abstract fun getButtons(): InlineKeyboardMarkup
}