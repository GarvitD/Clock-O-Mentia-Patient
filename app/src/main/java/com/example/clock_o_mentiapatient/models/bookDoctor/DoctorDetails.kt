package com.example.clock_o_mentiapatient.models.bookDoctor

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DoctorDetails(
    val id : String?,
    val name : String?,
    val email : String?,
    val age : Int?,
    val gender : String?,
    val address : String?,
    val phoneNumber : String?,
    val certificateUrl : String?,
    val profileImageUrl : String?,
    var distance : Double? = 0.0,
    val rating : Float = 3.5f
) : Parcelable
