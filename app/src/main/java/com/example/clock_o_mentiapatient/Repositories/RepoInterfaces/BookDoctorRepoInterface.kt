package com.example.clock_o_mentiapatient.Repositories.RepoInterfaces

import com.example.clock_o_mentiapatient.models.bookDoctor.DoctorListResponse
import com.example.clock_o_mentiapatient.models.ResponseWrapper

interface BookDoctorRepoInterface {
    suspend fun getDoctorsList() : ResponseWrapper<DoctorListResponse>
}