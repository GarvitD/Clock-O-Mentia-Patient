package com.example.clock_o_mentiapatient.models.authentication

data class AuthResponse(
    val tokens: Tokens,
    val user: User
)