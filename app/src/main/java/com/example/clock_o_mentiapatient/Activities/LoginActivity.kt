package com.example.clock_o_mentiapatient.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.clock_o_mentiapatient.models.authentication.LoginDetails
import com.example.clock_o_mentiapatient.models.NetworkState
import com.example.clock_o_mentiapatient.R
import com.example.clock_o_mentiapatient.Utils.*
import com.example.clock_o_mentiapatient.ViewModels.AuthViewModel
import com.example.clock_o_mentiapatient.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val loginViewModel by viewModels<AuthViewModel>()
    private lateinit var activityLoginBinding : ActivityLoginBinding
    private val progressDialog = ProgressDialogClass(this)
    private val sharedPreferenceManager = SharedPreferenceManager(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initObservers()

        activityLoginBinding.directToRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

        activityLoginBinding.loginBtn.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val password = activityLoginBinding.password.text.toString()
        val email = activityLoginBinding.emailAddress.text.toString()

        val loginDetails = LoginDetails(email, password)

        when (val error = loginViewModel.checkLoginData(loginDetails)) {
            ValidationError.EMPTY_EMAIL -> activityLoginBinding.inputEmailAddress.error = getString(error.code)
            ValidationError.EMPTY_PASSWORD -> activityLoginBinding.inputPassword.error = getString(error.code)
            else -> loginViewModel.login(loginDetails)
        }
    }

    private fun initObservers() {
        loginViewModel.loginResponse.observe(this) { response ->
            fun handleLoading() {
                progressDialog.startLoading()
            }

            fun handleSuccess() {
                progressDialog.dismiss()

                if (response.code == ResponseCode.CODE_201) {

                    progressDialog.dismiss()
                    response.data?.user?.let {
                        sharedPreferenceManager
                            .saveUserDetails(getString(R.string.user_details),it.toString())
                        response.data?.tokens?.access?.token?.let {
                            sharedPreferenceManager.
                            saveUserDetails(getString(R.string.auth_token), it)
                        }
                    }
                    startActivity(Intent(this@LoginActivity,NearbyDoctorsActivity::class.java))
                    finish()
                }

            }

            fun handleError() {
                progressDialog.dismiss()
                HelperClass.toast(this,response.message)
            }

            when (response.networkState) {
                NetworkState.LOADING -> handleLoading()
                NetworkState.SUCCESS -> handleSuccess()
                NetworkState.ERROR -> handleError()
            }

        }
    }
}