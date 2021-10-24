package com.example.services

import com.example.hikari
import com.example.domain.models.User
import java.lang.Exception
import java.sql.ResultSet


class AuthService {

    fun getUsers(): List<User> {
        try {
            val conn = hikari().getConnection()
            val stmt = conn.prepareCall("select * from user");
            stmt.execute();
            var res: MutableList<User> = mutableListOf()
            val rs: ResultSet = stmt.getObject(1) as ResultSet
            while (rs.next()) {
                res.add(User(rs.getLong(rs.findColumn("id")), rs.getString(rs.findColumn("name")), rs.getString(rs.findColumn("password"))))
            }
            rs.close();
            stmt.close();
            return res
        } catch (e: Exception) {
            println(e)
        }
        return mutableListOf()
    }
}

