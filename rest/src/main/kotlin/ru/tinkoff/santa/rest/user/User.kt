package ru.tinkoff.santa.rest.user

data class User(
    val id: Int,
    val nickname: String,
    val email: String,
    val password: ByteArray,
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val avatarUrl: String?,
    val telegramId: Long?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (nickname != other.nickname) return false
        if (email != other.email) return false
        if (!password.contentEquals(other.password)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + nickname.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.contentHashCode()
        return result
    }
}