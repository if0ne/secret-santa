package ru.tinkoff.santa.rest.user.registration

import ru.tinkoff.sanata.shared_models.status.RegistrationStatusCode

class RegistrationException(val statusCode: RegistrationStatusCode) : Exception()