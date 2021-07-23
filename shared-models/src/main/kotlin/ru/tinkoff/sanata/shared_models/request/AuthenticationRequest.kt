package ru.tinkoff.sanata.shared_models.request

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationRequest(val login: String, val password: String)
