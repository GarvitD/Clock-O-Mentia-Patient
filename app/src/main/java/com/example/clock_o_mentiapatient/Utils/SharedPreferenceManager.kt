package com.example.clock_o_mentiapatient.Utils

import android.content.Context

class SharedPreferenceManager(context1: Context?) {

    private var mInstance: SharedPreferenceManager? = null
    private var context: Context? = context1

    private val USER_DETAIL_SP_NAME = "userDetailsSp"
    private val USER_LOGIN_DETAILS = "userLoginSp"

    @Synchronized
    fun getInstance(context: Context?): SharedPreferenceManager? {
        if (mInstance == null) {
            mInstance = SharedPreferenceManager(context)
        }
        return mInstance
    }

    fun saveUserDetails(key: String, value: String) {
        context!!.getSharedPreferences(USER_DETAIL_SP_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(key, value)
            .apply()
    }

    fun getUserDetails(key: String): String? {
        return context!!.getSharedPreferences(USER_DETAIL_SP_NAME, Context.MODE_PRIVATE)
            .getString(key, "")
    }

    fun getLoginStatus(): Boolean {
        return context!!
            .getSharedPreferences(USER_LOGIN_DETAILS, Context.MODE_PRIVATE)
            .getBoolean("isLoggedIn", false)
    }

    fun setLoginStatus(isLoggedIn: Boolean) {
        context!!
            .getSharedPreferences(USER_LOGIN_DETAILS, Context.MODE_PRIVATE)
            .edit()
            .putBoolean("isLoggedIn", isLoggedIn)
            .apply()
    }
}