package ru.rsreu

data class AppConfig(
    val http: HttpConfig,
    val telegram: TelegramConfig,
    val server: ServerConfig
)

data class HttpConfig(
    val port: Int
)

data class TelegramConfig(
    val token: String,
    val webhookUrl: String
)

data class ServerConfig(
    val url: String,
    val guidCreateRoute: String
)