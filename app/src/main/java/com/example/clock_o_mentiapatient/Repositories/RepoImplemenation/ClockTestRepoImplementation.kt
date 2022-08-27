package com.example.clock_o_mentiapatient.Repositories.RepoImplemenation

import com.example.clock_o_mentiapatient.models.DimentiaTest.TestResponse
import com.example.clock_o_mentiapatient.models.ResponseWrapper
import com.example.clock_o_mentiapatient.Repositories.RepoInterfaces.ClockTestRepoInterface
import com.example.clock_o_mentiapatient.Retrofit.ApiServices
import okhttp3.MultipartBody
import javax.inject.Inject

class ClockTestRepoImplementation @Inject constructor(var apiServices: ApiServices) : ClockTestRepoInterface{

    override suspend fun getTestResult(part: MultipartBody.Part): ResponseWrapper<TestResponse> {
        return apiServices.getTestResult(part)
    }

}