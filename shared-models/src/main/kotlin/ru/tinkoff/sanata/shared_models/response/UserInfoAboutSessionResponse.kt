package ru.tinkoff.sanata.shared_models.response

import kotlinx.serialization.Serializable
import ru.tinkoff.sanata.shared_models.model.Gift
import ru.tinkoff.sanata.shared_models.model.Session
import ru.tinkoff.sanata.shared_models.model.User

@Serializable
data class UserInfoAboutSessionResponse(
    val user: User,
    val session: Session,
    val users: List<User>,
    val userGifts: List<Gift>,
    val giftReceivingUser: User?,
    val receivingUserGifts: List<Gift>
)
