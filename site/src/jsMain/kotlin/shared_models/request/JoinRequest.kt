package shared_models.request

import kotlinx.serialization.Serializable

@Serializable
data class JoinRequest(
    val userId: Int,
    val sessionId: Int
)