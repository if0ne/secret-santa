package shared_models.response

import kotlinx.serialization.Serializable
import shared_models.model.Gift
import shared_models.model.Session
import shared_models.model.User

@Serializable
data class UserInfoAboutSessionResponse(
    val user: User,
    val session: Session,
    val users: List<User>,
    val userGifts: List<Gift>,
    val giftReceivingUser: User?,
    val receivingUserGifts: List<Gift>
)
