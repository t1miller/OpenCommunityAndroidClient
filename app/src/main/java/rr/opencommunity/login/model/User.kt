package rr.opencommunity.login.model

data class User(
    val password: String,
    val email: String,
    val token: String
)