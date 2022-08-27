package com.example.clock_o_mentiapatient.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.clock_o_mentiapatient.R
import com.example.clock_o_mentiapatient.databinding.ActivityBookAppointmentBinding

class BookAppointmentActivity : AppCompatActivity() {

    private lateinit var activityBookAppointmentBinding: ActivityBookAppointmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBookAppointmentBinding = DataBindingUtil.setContentView(this,R.layout.activity_book_appointment)


    }
}