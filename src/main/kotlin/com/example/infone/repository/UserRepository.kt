package com.example.infone.repository

import com.example.infone.model.User

interface UserRepository {
    fun createUserTable()
    fun getUser(email: String): User?
    fun insertUser(id: String, email: String, password: String, dataPointsSelected: String)
}