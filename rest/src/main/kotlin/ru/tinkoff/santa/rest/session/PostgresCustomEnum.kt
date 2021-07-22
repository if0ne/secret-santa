package ru.tinkoff.santa.rest.session

import org.postgresql.util.PGobject

class PostgresCustomEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}