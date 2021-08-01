package shared_models.model

import kotlinx.serialization.Serializable

@Serializable
data class Gift(val id: Int, val pictureUrl: String?, val name: String, val description: String?)