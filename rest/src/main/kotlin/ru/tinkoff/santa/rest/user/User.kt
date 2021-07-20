package ru.tinkoff.santa.rest.user

import java.util.*

data class User(
    val id: Int,
    val telegramGuid: UUID?,
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
        if (telegramGuid != other.telegramGuid) return false
        if (nickname != other.nickname) return false
        if (email != other.email) return false
        if (!password.contentEquals(other.password)) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (middleName != other.middleName) return false
        if (avatarUrl != other.avatarUrl) return false
        if (telegramId != other.telegramId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + telegramGuid.hashCode()
        result = 31 * result + nickname.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.contentHashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + (middleName?.hashCode() ?: 0)
        result = 31 * result + (avatarUrl?.hashCode() ?: 0)
        result = 31 * result + (telegramId?.hashCode() ?: 0)
        return result
    }

}