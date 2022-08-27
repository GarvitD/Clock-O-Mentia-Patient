package com.example.clock_o_mentiapatient.models.bookDoctor

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppointmentDetails(
    val name : String,
    val date : String,
    val isWaiting : Boolean,
    val detailedInfo : String = "",
    val reportUrl : String,
    val doctorName : String,
    val doctorEmail : String,
    val meetingId : String
) : Parcelable
