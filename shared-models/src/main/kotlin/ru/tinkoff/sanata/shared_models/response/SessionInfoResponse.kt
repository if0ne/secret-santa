package ru.tinkoff.sanata.shared_models.response

import kotlinx.serialization.Serializable
import ru.tinkoff.sanata.shared_models.model.Gift
import ru.tinkoff.sanata.shared_models.model.Session
import ru.tinkoff.sanata.shared_models.model.User

@Serializable
data class SessionInfoResponse(
    val session: Session,
    val usersGifts: List<Pair<User, List<Gift>>>
)
