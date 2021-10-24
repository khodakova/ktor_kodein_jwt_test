package com.example

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import org.jetbrains.exposed.sql.Database
import java.sql.Connection
import java.sql.SQLException


//fun Application.initDB(): Connection {
//    return hikari().getConnection()
//}

// подключение к базе через exposed
fun Application.initDB() {
    Database.connect(hikari())
}

fun hikari(): HikariDataSource {
    val config = HikariConfig();
    config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
    config.setUsername("postgres");
    config.setPassword("123");
    config.addDataSourceProperty("databaseName", "test_db");
    config.addDataSourceProperty("serverName", "localhost");
    config.addDataSourceProperty("portNumber", 5432);
    return HikariDataSource(config);
}