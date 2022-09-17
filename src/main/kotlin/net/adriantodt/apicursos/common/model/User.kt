package net.adriantodt.apicursos.common.model

data class User(
    val userId: Long,
    val userType: UserType,

    var username: String,
    var hashedPassword: String,
    var name: String,
    var email: String
)