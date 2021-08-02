package shared_models.model

import kotlinx.serialization.Serializable

@Serializable
data class ChangeNotification(
    val session: Session,
    val users: List<User>
)