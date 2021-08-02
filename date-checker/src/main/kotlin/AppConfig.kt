data class AppConfig(
    val server: ServerConfig,
    val telegram: TelegramConfig
)

data class ServerConfig(
    val url: String
)

data class TelegramConfig(
    val url: String
)