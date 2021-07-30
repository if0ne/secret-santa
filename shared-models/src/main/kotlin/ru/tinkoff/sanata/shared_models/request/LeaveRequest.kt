package ru.tinkoff.sanata.shared_models.request

import kotlinx.serialization.Serializable

@Serializable
data class LeaveRequest(
    val userId: Int,
    val sessionId: Int
)
