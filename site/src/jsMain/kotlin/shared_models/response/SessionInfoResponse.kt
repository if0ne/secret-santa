package shared_models.response

import kotlinx.serialization.Serializable

import shared_models.model.Gift
import shared_models.model.Session
import shared_models.model.User

@Serializable
data class SessionInfoResponse(
    val session: Session,
    val usersGifts: List<Pair<User, List<Gift>>>
)
