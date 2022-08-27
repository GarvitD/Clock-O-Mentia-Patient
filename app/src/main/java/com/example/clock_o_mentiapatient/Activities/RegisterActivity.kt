package com.example.clock_o_mentiapatient.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.clock_o_mentiapatient.models.NetworkState
import com.example.clock_o_mentiapatient.models.authentication.RegisterDetails
import com.example.clock_o_mentiapatient.R
import com.example.clock_o_mentiapatient.Utils.*
import com.example.clock_o_mentiapatient.ViewModels.AuthViewModel
import com.example.clock_o_mentiapatient.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val registerViewModel by viewModels<AuthViewModel>()
    private lateinit var activityRegisterBinding: ActivityRegisterBinding
    private val progressDialog = ProgressDialogClass(this)
    private val sharedPreferenceManager = SharedPreferenceManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRegisterBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        initObservers()

        activityRegisterBinding.signUpBtn.setOnClickListener {
            registerUser()
        }

        activityRegisterBinding.directToLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
        }
    }

    private fun registerUser() {
        val name = activityRegisterBinding.fullName.text.toString()
        val age = activityRegisterBinding.age.text.toString()
        val password = activityRegisterBinding.password.text.toString()
        val email = activityRegisterBinding.emailAddress.text.toString().trim()
        val confPassword = activityRegisterBinding.confirmPassword.text.toString()

        if(password != confPassword) {
            activityRegisterBinding.inputConfirmPassword.error = getString(R.string.password_not_match)
            return
        }
        val registerDetails = RegisterDetails(name,email,age.toInt(),password)

        when (val error = registerViewModel.checkSignUpData(registerDetails)) {
            ValidationError.EMPTY_EMAIL -> activityRegisterBinding.inputEmailAddress.error = getString(error.code)
            ValidationError.EMPTY_PASSWORD -> activityRegisterBinding.inputPassword.error = getString(error.code)
            ValidationError.EMPTY_NAME -> activityRegisterBinding.inputFullName.error = getString(error.code)
            ValidationError.INVALID_AGE -> activityRegisterBinding.inputAge.error = getString(error.code)
            ValidationError.INVALID_PASSWORD -> activityRegisterBinding.inputPassword.error = getString(error.code)
            ValidationError.INVALID_EMAIL -> activityRegisterBinding.inputEmailAddress.error = getString(error.code)
            else -> registerViewModel.register(registerDetails)
        }
    }

    private fun initObservers() {
        registerViewModel.registerResponse.observe(this) { response ->
            fun handleLoading() {
                progressDialog.startLoading()
            }

            fun handleSuccess() {
                progressDialog.dismiss()

                if (response.code == ResponseCode.CODE_201) {
                    progressDialog.dismiss()
                    sharedPreferenceManager
                        .saveUserDetails(getString(R.string.email_address),activityRegisterBinding.emailAddress.text.toString())
                    sharedPreferenceManager
                        .saveUserDetails(getString(R.string.age),activityRegisterBinding.age.text.toString())
                    startActivity(Intent(this@RegisterActivity,NearbyDoctorsActivity::class.java))
                    finish()
                }

            }

            fun handleError() {
                progressDialog.dismiss()
                if(response.code == ResponseCode.CODE_411) {
                    activityRegisterBinding.inputEmailAddress.error = response.message
                } else {
                    HelperClass.toast(this,response.message)
                }
            }

            when (response.networkState) {
                NetworkState.LOADING -> handleLoading()
                NetworkState.SUCCESS -> handleSuccess()
                NetworkState.ERROR -> handleError()
            }

        }
    }
}