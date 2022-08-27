package com.example.clock_o_mentiapatient.Repositories.RepoInterfaces

import com.example.clock_o_mentiapatient.models.DimentiaTest.TestResponse
import com.example.clock_o_mentiapatient.models.ResponseWrapper
import okhttp3.MultipartBody

interface ClockTestRepoInterface {
    suspend fun getTestResult(part : MultipartBody.Part) : ResponseWrapper<TestResponse>
}