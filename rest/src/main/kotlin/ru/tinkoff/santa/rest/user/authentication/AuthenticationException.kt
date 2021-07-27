package ru.tinkoff.santa.rest.user.authentication

import ru.tinkoff.sanata.shared_models.status.AuthenticationStatusCode

class AuthenticationException(val statusCode: AuthenticationStatusCode) : Exception()