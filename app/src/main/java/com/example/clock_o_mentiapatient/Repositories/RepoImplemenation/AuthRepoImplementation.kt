package com.example.clock_o_mentiapatient.Repositories.RepoImplemenation

import com.example.clock_o_mentiapatient.models.authentication.AuthResponse
import com.example.clock_o_mentiapatient.models.authentication.LoginDetails
import com.example.clock_o_mentiapatient.models.authentication.RegisterDetails
import com.example.clock_o_mentiapatient.models.ResponseWrapper
import com.example.clock_o_mentiapatient.Repositories.RepoInterfaces.AuthRepoInterface
import com.example.clock_o_mentiapatient.Retrofit.ApiServices
import javax.inject.Inject

class AuthRepoImplementation @Inject constructor(var apiServices: ApiServices) : AuthRepoInterface{
    override suspend fun signUp(registerDetails: RegisterDetails): ResponseWrapper<AuthResponse> {
        return apiServices.signUp(registerDetails)
    }

    override suspend fun login(loginDetails: LoginDetails): ResponseWrapper<AuthResponse> {
        return apiServices.login(loginDetails)
    }
}