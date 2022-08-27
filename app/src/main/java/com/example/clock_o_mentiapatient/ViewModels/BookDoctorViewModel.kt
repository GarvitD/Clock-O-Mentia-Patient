package com.example.clock_o_mentiapatient.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clock_o_mentiapatient.models.bookDoctor.DoctorListResponse
import com.example.clock_o_mentiapatient.models.ResponseWrapper
import com.example.clock_o_mentiapatient.Repositories.RepoInterfaces.BookDoctorRepoInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDoctorViewModel @Inject constructor(var bookDoctorRepoInterface: BookDoctorRepoInterface) : ViewModel(){

    var doctorsListResponse = MutableLiveData<ResponseWrapper<DoctorListResponse>>()

    fun getDoctorsList() {
        viewModelScope.launch {
            doctorsListResponse.postValue(ResponseWrapper.loading())
            try {
                val response = bookDoctorRepoInterface.getDoctorsList()
                doctorsListResponse.postValue(ResponseWrapper.success(response))

            } catch (e: Exception) {
                doctorsListResponse.postValue(ResponseWrapper.error(e.toString()))
            }
        }
    }
}