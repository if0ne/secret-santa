package ru.tinkoff.santa.rest.gift

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import ru.tinkoff.sanata.shared_models.model.Gift

class GiftDao(private val database: Database) {
    fun getAll(): List<Gift> = transaction(database) {
        Gifts.selectAll().map(::extractGift)
    }

    fun getById(id: Int): Gift? = transaction(database) {
        runCatching {
            extractGift(Gifts.select { Gifts.id eq id }.first())
        }.getOrNull()
    }

    fun create(pictureUrl: String?, name: String, description: String?): Int = transaction(database) {
        Gifts.insertAndGetId {
            wrapGiftToUpdateBuilder(it, pictureUrl, name, description)
        }
    }.value

    fun update(id: Int, pictureUrl: String?, name: String, description: String?) = transaction(database) {
        Gifts.update({ Gifts.id eq id }) {
            wrapGiftToUpdateBuilder(it, pictureUrl, name, description)
        }
    }

    fun delete(id: Int) = transaction(database) {
        Gifts.deleteWhere { Gifts.id eq id }
    }
}


private fun wrapGiftToUpdateBuilder(
    updateBuilder: UpdateBuilder<Number>,
    pictureUrl: String?,
    name: String,
    description: String?
) {
    updateBuilder[Gifts.pictureUrl] = pictureUrl
    updateBuilder[Gifts.name] = name
    updateBuilder[Gifts.description] = description
}

private fun extractGift(row: ResultRow): Gift = Gift(
    row[Gifts.id].value,
    row[Gifts.pictureUrl],
    row[Gifts.name],
    row[Gifts.description]
)