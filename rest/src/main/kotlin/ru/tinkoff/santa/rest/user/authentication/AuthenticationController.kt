package ru.tinkoff.santa.rest.user.authentication

import ru.tinkoff.sanata.shared_models.status.AuthenticationStatusCode
import ru.tinkoff.santa.rest.user.User
import ru.tinkoff.santa.rest.user.UserService
import java.util.*

class AuthenticationController(private val userService: UserService) {

    fun authorize(login: String, password: String): User {
        val user: User =
            userService.getByEmail(login) ?: (userService.getByNickname(login) ?: throw AuthenticationException(
                AuthenticationStatusCode.USER_NAME_NOT_EXISTS
            ))
        return if (isPasswordValid(password, user.password)) user else throw AuthenticationException(
            AuthenticationStatusCode.INVALID_PASSWORD
        )
    }

    private fun isPasswordValid(inputPassword: String, encodePassword: ByteArray): Boolean =
        inputPassword == decodePassword(encodePassword)

    private fun decodePassword(password: ByteArray): String = String(Base64.getDecoder().decode(password))
}
