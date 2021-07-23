package ru.rsreu

data class AppConfig(
    val http: HttpConfig,
    val telegram: TelegramConfig
)

data class HttpConfig(
    val port: Int
)

data class TelegramConfig(
    val token: String,
    val webhookUrl: String
)