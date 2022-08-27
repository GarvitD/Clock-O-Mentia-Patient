package com.example.clock_o_mentiapatient.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.ceylonlabs.imageviewpopup.ImagePopup
import com.example.clock_o_mentiapatient.R
import com.example.clock_o_mentiapatient.databinding.ActivityAppointmentInfoBinding
import com.example.clock_o_mentiapatient.models.bookDoctor.AppointmentDetails

class AppointmentInfoActivity : AppCompatActivity() {

    private lateinit var activityAppointmentInfoBinding: ActivityAppointmentInfoBinding
    private lateinit var appointmentDetails: AppointmentDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAppointmentInfoBinding = DataBindingUtil.setContentView(this,R.layout.activity_appointment_info)

        intent.getParcelableExtra<AppointmentDetails>(getString(R.string.selectedAppointment))?.let {
            appointmentDetails = it
        }

        activityAppointmentInfoBinding.appointmentReport.setOnClickListener {
            showReport()
        }

        activityAppointmentInfoBinding.videoCall.setOnClickListener {
            val intent = Intent(this,VideoCallActivity::class.java)
            intent.putExtra(getString(R.string.meetingId),appointmentDetails.meetingId)
            startActivity(intent)
        }
    }

    private fun showReport() {

        val imagePopup = ImagePopup(this)
        imagePopup.windowHeight = 800
        imagePopup.windowWidth = 800
        imagePopup.backgroundColor = ContextCompat.getColor(this, R.color.black)
        imagePopup.isFullScreen = true
        imagePopup.isHideCloseIcon = true
        imagePopup.isImageOnClickClose = true
        imagePopup.initiatePopupWithPicasso(appointmentDetails.reportUrl)
        imagePopup.viewPopup()

    }
}