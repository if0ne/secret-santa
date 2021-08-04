package ru.rsreu.bot

enum class SessionCreatingState {
    ENTER_DESCRIPTION,
    ENTER_BUDGET,
    ENTER_MIN_PLAYERS,
    ENTER_EVENT_TIMESTAMP,
    ENTER_TIMESTAMP_TO_CHOOSE
}