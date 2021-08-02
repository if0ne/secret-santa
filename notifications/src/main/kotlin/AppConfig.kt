data class AppConfig(
    val ticker: TickerConfig,
    val server: ServerConfig,
    val telegram: TelegramConfig
)

data class TickerConfig(
    val delay: Long
)

data class ServerConfig(
    val url: String,
    val notificationRoute: String
)

data class TelegramConfig(
    val url: String,
    val notificationRoute: String
)