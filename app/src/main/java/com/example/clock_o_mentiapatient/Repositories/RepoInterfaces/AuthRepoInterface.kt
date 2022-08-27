package com.example.clock_o_mentiapatient.Repositories.RepoInterfaces

import com.example.clock_o_mentiapatient.models.authentication.AuthResponse
import com.example.clock_o_mentiapatient.models.authentication.LoginDetails
import com.example.clock_o_mentiapatient.models.authentication.RegisterDetails
import com.example.clock_o_mentiapatient.models.ResponseWrapper

interface AuthRepoInterface {
    suspend fun signUp(registerDetails: RegisterDetails) : ResponseWrapper<AuthResponse>
    suspend fun login(loginDetails: LoginDetails) : ResponseWrapper<AuthResponse>
}