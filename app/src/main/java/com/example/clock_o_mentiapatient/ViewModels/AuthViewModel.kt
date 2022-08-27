package com.example.clock_o_mentiapatient.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clock_o_mentiapatient.models.authentication.AuthResponse
import com.example.clock_o_mentiapatient.models.authentication.LoginDetails
import com.example.clock_o_mentiapatient.models.authentication.RegisterDetails
import com.example.clock_o_mentiapatient.models.ResponseWrapper
import com.example.clock_o_mentiapatient.Repositories.RepoInterfaces.AuthRepoInterface
import com.example.clock_o_mentiapatient.Utils.ValidationError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(var authRepoInterface: AuthRepoInterface) :
    ViewModel(){

    var registerResponse = MutableLiveData<ResponseWrapper<AuthResponse>>()
    var loginResponse = MutableLiveData<ResponseWrapper<AuthResponse>>()
    var authToken : String? = null

    fun checkSignUpData(registerDetails: RegisterDetails) : ValidationError {
        if(registerDetails.email.isNullOrEmpty()) {
            return ValidationError.EMPTY_EMAIL
        }
        if(registerDetails.password.isNullOrEmpty()) {
            return ValidationError.EMPTY_PASSWORD
        }
        if(registerDetails.name.isNullOrEmpty()) {
            return ValidationError.EMPTY_NAME
        }
        if(registerDetails.age == null || registerDetails.age <= 0) {
            return ValidationError.INVALID_AGE
        }
        if(isPasswordInvalid(registerDetails.password)) {
            return ValidationError.INVALID_PASSWORD
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(registerDetails.email).matches()) {
            return ValidationError.INVALID_EMAIL
        }
        return ValidationError.NO_ERROR
    }

    private fun isPasswordInvalid(password: String): Boolean {
        var isInvalid = false
        if(password.length < 8 ||
            (password.filter { Character.isDigit(it) }).isEmpty() ||
            (password.filter { Character.isLetter(it) }).isEmpty()) { isInvalid = true }
        return isInvalid
    }

    fun checkLoginData(loginDetails: LoginDetails) : ValidationError {
        if(loginDetails.email.isNullOrEmpty()) {
            return ValidationError.EMPTY_EMAIL
        }
        if(loginDetails.password.isNullOrEmpty()) {
            return ValidationError.EMPTY_PASSWORD
        }
        return ValidationError.NO_ERROR
    }

    fun register(registerDetails: RegisterDetails) {
        viewModelScope.launch {
            registerResponse.postValue(ResponseWrapper.loading())
            try {
                val response = authRepoInterface.signUp(registerDetails)
                authToken = response.data.toString()
                registerResponse.postValue(ResponseWrapper.success(response))

            } catch (e: Exception) {
                registerResponse.postValue(ResponseWrapper.error(e.toString()))
            }
        }
    }

    fun login(loginDetails: LoginDetails) {
        viewModelScope.launch {
            loginResponse.postValue(ResponseWrapper.loading())
            try {
                val response = authRepoInterface.login(loginDetails)
                authToken = response.data.toString()
                loginResponse.postValue(ResponseWrapper.success(response))

            } catch (e: Exception) {
                loginResponse.postValue(ResponseWrapper.error(e.toString()))
            }
        }
    }
}