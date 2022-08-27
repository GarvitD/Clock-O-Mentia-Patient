package com.example.clock_o_mentiapatient.Activities

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import com.ceylonlabs.imageviewpopup.ImagePopup
import com.example.clock_o_mentiapatient.R
import com.example.clock_o_mentiapatient.Utils.HelperClass
import com.example.clock_o_mentiapatient.databinding.ActivityViewDoctorBinding
import com.example.clock_o_mentiapatient.models.bookDoctor.DoctorDetails
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil
import java.io.IOException

class ViewDoctorActivity : AppCompatActivity(),OnMapReadyCallback {

    private lateinit var activityViewDoctorBinding: ActivityViewDoctorBinding
    private lateinit var doctorDetails : DoctorDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityViewDoctorBinding = DataBindingUtil.setContentView(this,R.layout.activity_view_doctor)

        val intent = intent
        intent.getParcelableExtra<DoctorDetails>(getString(R.string.viewSelectedDoctor))?.let {
            doctorDetails = it
        }

        activityViewDoctorBinding.doctorDetails = doctorDetails

        (supportFragmentManager.findFragmentById(R.id.google_map_view_doctor) as SupportMapFragment).getMapAsync(this)

        activityViewDoctorBinding.bookAppointment.setOnClickListener {
            val sendDataIntent = Intent(this,BookAppointmentActivity::class.java)
            sendDataIntent.putExtra(getString(R.string.viewSelectedDoctor),doctorDetails)
            startActivity(sendDataIntent)
        }

        activityViewDoctorBinding.doctorCertificate.setOnClickListener {
            showCertificate()
        }
    }

    private fun showCertificate() {

        val imagePopup = ImagePopup(this)
        imagePopup.windowHeight = 800
        imagePopup.windowWidth = 800
        imagePopup.background = AppCompatResources.getDrawable(this,R.drawable.default_profile_img)
        imagePopup.isFullScreen = true
        imagePopup.isHideCloseIcon = true
        imagePopup.isImageOnClickClose = true
        imagePopup.initiatePopupWithPicasso(doctorDetails.certificateUrl)
        imagePopup.viewPopup()

    }

    override fun onMapReady(googleMap: GoogleMap) {

        val geocoder = Geocoder(this)
        try {
            val addressList = geocoder.getFromLocationName(doctorDetails.address,1)
            if (addressList != null) {
                val latitude = addressList[0].latitude
                val longitude = addressList[0].longitude
                val markerOptions = MarkerOptions().title(getString(R.string.doctor_location)).position(
                    LatLng(latitude, longitude)
                )
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude)
                    , 17f))
                googleMap.addMarker(markerOptions)
            }
        } catch (e: IOException) {
            HelperClass.toast(this,e.toString())
        }

    }
}