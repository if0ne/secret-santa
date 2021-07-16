package ru.tinkoff.santa.rest

data class AppConfig(val httpConfig: HttpConfig, val dataBaseConfig: DataBaseConfig)

data class HttpConfig(val host: String, val port: Int)

data class DataBaseConfig(val url: String, val user: String, val password: String)
