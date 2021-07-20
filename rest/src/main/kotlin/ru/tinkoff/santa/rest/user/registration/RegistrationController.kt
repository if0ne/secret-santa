package ru.tinkoff.santa.rest.user.registration

import ru.tinkoff.sanata.shared_models.model.User
import ru.tinkoff.sanata.shared_models.status.RegistrationStatusCode
import ru.tinkoff.santa.rest.user.UserService
import java.util.*

class RegistrationController(private val userService: UserService) {

    companion object {
        private const val regexPattern: String =
            "(?=.*[0-9])(?=.*[!@#\$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#\$%^&*]{6,}"
        private val passwordRegex = Regex(regexPattern)

        private fun String.encodePassword(): ByteArray = Base64.getEncoder().encode(this.toByteArray())

        fun String.isPasswordSafety(): Boolean = passwordRegex.containsMatchIn(this)
    }

    fun register(
        nickname: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        middleName: String?
    ): User {
        if (userService.getByNickname(nickname) != null) {
            throw RegistrationException(RegistrationStatusCode.NICKNAME_NOT_AVAILABLE)
        }
        if (userService.getByEmail(email) != null) {
            throw RegistrationException(RegistrationStatusCode.EMAIL_NOT_AVAILABLE)
        }
        return userService.create(
            null,
            nickname,
            email,
            password.encodePassword(),
            firstName,
            lastName,
            middleName,
            null,
            null
        )
    }
}