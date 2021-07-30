package ru.tinkoff.santa.rest.user_session

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(val id: Int, val userId: Int, val sessionId: Int)