package shared_models.request

import kotlinx.serialization.Serializable

@Serializable
data class JoinByGuidRequest(
    val userId: Int,
    val sessionGuid: String
)
