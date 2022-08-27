package com.example.clock_o_mentiapatient.Repositories.RepoImplemenation

import com.example.clock_o_mentiapatient.models.bookDoctor.DoctorListResponse
import com.example.clock_o_mentiapatient.models.ResponseWrapper
import com.example.clock_o_mentiapatient.Repositories.RepoInterfaces.BookDoctorRepoInterface
import com.example.clock_o_mentiapatient.Retrofit.ApiServices
import javax.inject.Inject

class BookDoctorImplementation @Inject constructor(var apiServices: ApiServices) : BookDoctorRepoInterface{
    override suspend fun getDoctorsList(): ResponseWrapper<DoctorListResponse> {
        return apiServices.getDoctorsList()
    }
}