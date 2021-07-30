package shared_models.model

import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val id: Int,
    val guid: String?,
    val currentState: SessionState,
    val description: String?,
    val hostId: Int,
    val budget: Int,
    val minPlayersQuantity: Int,
    val eventTimestamp: String,
    val timestampToChoose: String
)