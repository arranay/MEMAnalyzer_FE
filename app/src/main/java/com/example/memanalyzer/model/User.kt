package com.example.memanalyzer.model

open class User {
    lateinit var id: String
    lateinit var userName: String
    lateinit var fullName: String
    lateinit var email: String
    lateinit var password: String
    lateinit var dateOfBirth: String
    var gender: Boolean = true
    lateinit var token: String

    var result: Result? = null
    lateinit var role: UserRole

    var lockoutEnd: String? = null
    var isLocked: Boolean? = null
}

enum class UserRole {
    Administrator, User
}