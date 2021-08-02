package ru.tinkoff.santa.rest.guid.exception

import ru.tinkoff.sanata.shared_models.status.GuidErrorCode

class GuidException(val errorCode: GuidErrorCode) : Exception()