package shared_models.request

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(
    val phone: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val middleName: String?
)