package com.example

import com.example.config.ModulesConfig
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.example.web.controllers.UserController
import com.example.web.routes.users
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.instance

fun main() {
    embeddedServer(Netty, port = 8081, host = "localhost") {
        initDB()
        val userController by ModulesConfig.di.instance<UserController>()
        configureSecurity(userController)
//        configureRouting(userController)
        configureSerialization()
        configureHTTP()

        routing {
            install(StatusPages) {
                exception<AuthenticationException> { cause ->
                    call.respond(HttpStatusCode.Unauthorized)
                }
                exception<AuthorizationException> { cause ->
                    call.respond(HttpStatusCode.Forbidden)
                }
            }
            users(userController)
        }

    }.start(wait = true)


    //    val conn = hikari().getConnection()
////    val stmt = conn.prepareCall("select * from users where name = 'vita'");
//    val stmt = conn.prepareStatement("select * from users where name = 'vita'");
////    stmt.execute();
//    val rs = stmt.executeQuery();
//    var res: MutableList<User> = mutableListOf()
////    val rs: ResultSet = stmt.getObject(1) as ResultSet
//    while (rs.next()) {
//        println(rs.getString(rs.findColumn("name")))
//        res.add(User(rs.getInt(rs.findColumn("id")), rs.getString(rs.findColumn("name")), rs.getString(rs.findColumn("password"))))
//    }
//    rs.close();
//    stmt.close();
//    conn.close();
}
