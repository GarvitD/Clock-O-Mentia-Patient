package com.example.clock_o_mentiapatient.Retrofit

import com.example.clock_o_mentiapatient.models.authentication.AuthResponse
import com.example.clock_o_mentiapatient.models.authentication.LoginDetails
import com.example.clock_o_mentiapatient.models.authentication.RegisterDetails
import com.example.clock_o_mentiapatient.models.bookDoctor.DoctorListResponse
import com.example.clock_o_mentiapatient.models.DimentiaTest.TestResponse
import com.example.clock_o_mentiapatient.models.ResponseWrapper
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiServices {

    @POST("/v1/auth/register")
    suspend fun signUp(
        @Body registerDetails: RegisterDetails
    ) : ResponseWrapper<AuthResponse>

    @POST("/v1/auth/login")
    suspend fun login(
        @Body loginDetails: LoginDetails
    ) : ResponseWrapper<AuthResponse>

    @Multipart
    @POST("/predict")
    suspend fun getTestResult(
        @Part file : MultipartBody.Part
    ): ResponseWrapper<TestResponse>

    @GET("v1/docInfos/")
    suspend fun getDoctorsList() : ResponseWrapper<DoctorListResponse>
}