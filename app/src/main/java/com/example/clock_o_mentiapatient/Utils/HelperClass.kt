package com.example.clock_o_mentiapatient.Utils

import android.content.Context
import android.widget.Toast

object HelperClass {
    fun toast(context: Context, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}