package com.example.clock_o_mentiapatient.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clock_o_mentiapatient.models.DimentiaTest.TestResponse
import com.example.clock_o_mentiapatient.models.ResponseWrapper
import com.example.clock_o_mentiapatient.Repositories.RepoInterfaces.ClockTestRepoInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ClockTestViewModel @Inject constructor(var clockTestRepoInterface: ClockTestRepoInterface) : ViewModel() {

    var clockTestResult  = MutableLiveData<ResponseWrapper<TestResponse>>()

    fun getClockTestResult(part : MultipartBody.Part) {
        viewModelScope.launch {
            clockTestResult.postValue(ResponseWrapper.loading())

            try {
                val response = clockTestRepoInterface.getTestResult(part)
                clockTestResult.postValue(response)
            } catch (e: Exception) {
                clockTestResult.postValue(ResponseWrapper.error(e.toString()))
            }
        }
    }
}