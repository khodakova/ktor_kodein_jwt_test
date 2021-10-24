package com.example.domain.repository

import com.example.domain.models.User
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

internal object Users : LongIdTable() {
    val username: Column<String> = varchar("username", 100).uniqueIndex()
    val email: Column<String> = varchar("email", 100).uniqueIndex()
    val password: Column<String> = varchar("password", 100)

    fun toUser(row: ResultRow): User {
        return User(
            id = row[Users.id].value,
            username = row[username],
            email = row[email],
            password = row[password]
        )
    }
}

class UserRepository {
    init {
        transaction {
            SchemaUtils.create(Users)
        }
    }

    fun findById(id: Long): User? {
        return transaction {
            Users.select { Users.id eq id }
                .map { Users.toUser(it) }
                .firstOrNull()
        }
    }

    fun findByEmail(email: String): User? {
        return transaction {
            Users.select { Users.email eq email }
                .map { Users.toUser(it) }
                .firstOrNull()
        }
    }

    fun findByUsername(username: String): User? {
        return transaction {
            Users.select { Users.username eq username }
                .map { Users.toUser(it) }
                .firstOrNull()
        }
    }

    fun create(user: User): Long? {
        return transaction {
            Users.insertAndGetId { row ->
                row[username] = user.username!!
                row[email] = user.email
                row[password] = user.password!!
            }.value
        }
    }

    fun update(email: String, user: User): User? {
        transaction {
            Users.update({ Users.email eq email }) { row ->
                row[Users.email] = user.email
                if (user.username != null) {
                    row[username] = user.username
                }
                if (user.password != null) {
                    row[password] = user.password
                }
            }
        }
        return findByEmail(user.email)
    }
}

