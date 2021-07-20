package ru.tinkoff.santa.rest.user.authentication

import ru.tinkoff.sanata.shared_models.status.AuthenticationStatusCode
import ru.tinkoff.sanata.shared_models.model.User
import ru.tinkoff.santa.rest.user.UserService
import java.util.*

class AuthenticationController(private val userService: UserService) {

    companion object {
        private fun ByteArray.decode() =
            String(Base64.getDecoder().decode(this))

        private fun String.isPasswordValid(encodePassword: ByteArray): Boolean = this == encodePassword.decode()
    }

    fun authenticate(login: String, password: String): User {
        val user: User =
            userService.getByEmail(login) ?: (userService.getByNickname(login) ?: throw AuthenticationException(
                AuthenticationStatusCode.USER_NAME_NOT_EXISTS
            ))
        return if (password.isPasswordValid(user.password)) user else throw AuthenticationException(
            AuthenticationStatusCode.INVALID_PASSWORD
        )
    }
}
