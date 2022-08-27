package com.example.clock_o_mentiapatient.Activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.clock_o_mentiapatient.Adapters.NearbyDoctorsAdapter
import com.example.clock_o_mentiapatient.models.bookDoctor.DoctorDetails
import com.example.clock_o_mentiapatient.models.NetworkState
import com.example.clock_o_mentiapatient.R
import com.example.clock_o_mentiapatient.Utils.HelperClass
import com.example.clock_o_mentiapatient.Utils.ProgressDialogClass
import com.example.clock_o_mentiapatient.Utils.ResponseCode
import com.example.clock_o_mentiapatient.ViewModels.BookDoctorViewModel
import com.example.clock_o_mentiapatient.databinding.ActivityNearbyDoctorsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil
import java.io.IOException
import java.util.*

class NearbyDoctorsActivity : AppCompatActivity(),NearbyDoctorsAdapter.NearbyDoctorsInterface {

    private lateinit var activityNearbyDoctorsBinding: ActivityNearbyDoctorsBinding
    private val progressDialog = ProgressDialogClass(this)
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var isAgainFetchReq = false
    private lateinit var supportMapFragment : SupportMapFragment
    private val bookDoctorViewModel by viewModels<BookDoctorViewModel>()
    private var patientCoordinates : LatLng? = null
    private var doctorsList : MutableList<DoctorDetails> = mutableListOf()
    private lateinit var nearbyDoctorsAdapter: NearbyDoctorsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityNearbyDoctorsBinding = DataBindingUtil.setContentView(this,R.layout.activity_nearby_doctors)

        supportMapFragment = (supportFragmentManager
            .findFragmentById(R.id.google_map) as SupportMapFragment?)!!

        retrieveUserLocation()

        initObservers()

        nearbyDoctorsAdapter = NearbyDoctorsAdapter(doctorsList,this)
        activityNearbyDoctorsBinding.doctorsListRecycler.setHasFixedSize(true)
        activityNearbyDoctorsBinding.doctorsListRecycler.adapter = nearbyDoctorsAdapter

    }

    private fun initObservers() {
        bookDoctorViewModel.doctorsListResponse.observe(this) { response ->
            fun handleLoading() {
                progressDialog.startLoading()
            }

            fun handleSuccess() {
                progressDialog.dismiss()

                if (response.code == ResponseCode.CODE_200) {
                    val responseList = response.data?.doctors

                    if (responseList != null && patientCoordinates != null) {

                        val geocoder = Geocoder(this)
                        doctorsList = responseList.toMutableList()
                        responseList.forEachIndexed { index, doctorDetails ->
                            try {
                                val addressList = geocoder.getFromLocationName(doctorDetails.address,1)
                                if (addressList != null) {
                                    val latitude = addressList[0].latitude
                                    val longitude = addressList[0].longitude
                                    doctorsList[index].distance = SphericalUtil.computeDistanceBetween(patientCoordinates,
                                        LatLng(latitude,longitude)
                                    )
                                }
                            } catch (e: IOException) {
                                HelperClass.toast(this,e.toString())
                            }
                        }
                        sortList()

                    }
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

    private fun sortList() {
        doctorsList.sortWith(Comparator<DoctorDetails> { t1, t2 ->
            return@Comparator (t1.distance!! - t2.distance!!).toInt()
        })
        nearbyDoctorsAdapter.updateList(doctorsList)
    }

    private fun retrieveUserLocation() {
        progressDialog.startLoading()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if(checkPermissions()) {
            if(isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProviderClient.lastLocation
                    .addOnCompleteListener(this) {
                        val location = it.result
                        if(location == null) {
                            HelperClass.toast(this,"Location received is Null")
                            requestLocation()
                        } else {
                            HelperClass.toast(this,"Latitude : ${location.latitude} , Longitude : ${location.longitude}")
                            showMap(LatLng(location.latitude,location.longitude))
                            Log.e("mapsTitle","Latitude : ${location.latitude} , Longitude : ${location.longitude}")
                            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
                            bookDoctorViewModel.getDoctorsList()
                        }
                        progressDialog.dismiss()
                        isAgainFetchReq = false

                    }
            } else {
                AlertDialog.Builder(this)
                    .setPositiveButton("OK"
                    ) { dialog, _ ->
                        HelperClass.toast(this,"Please Enable Location")
                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel"
                    ) { dialog, _ -> dialog?.dismiss() }
                    .setTitle("Location is disabled, Please enable to continue")
                    .show()
                progressDialog.dismiss()
            }
        } else {
            requestPermission()
        }
    }

    private fun showMap(latLng: LatLng) {
        supportMapFragment.getMapAsync {
            val geocoder = Geocoder(this, Locale.getDefault())
            var addressString= ""

            try {
                val addressList =
                    geocoder.getFromLocation(latLng.latitude,latLng.longitude,1)
                if (!addressList.isNullOrEmpty()) {
                    val address = addressList[0]
                    val sb = StringBuilder()
                    for (i in 0 until address.maxAddressLineIndex) {
                        sb.append(address.getAddressLine(i)).append(" ")
                    }

                    if (address.premises != null)
                        sb.append(address.premises).append(", ")

                    sb.append(address.subAdminArea).append(" ")
                    sb.append(address.locality).append(", ")
                    sb.append(address.adminArea).append(", ")
                    sb.append(address.countryName).append(", ")
                    sb.append(address.postalCode)

                    addressString = sb.toString()
                    Log.e("mapsTitle",addressString)
                }
            } catch (e: IOException) {
                HelperClass.toast(this,e.message)
            }

            val options = MarkerOptions().position(latLng)
                .title(addressString)
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
            it.addMarker(options)?.showInfoWindow()
            it.setInfoWindowAdapter(object : InfoWindowAdapter {
                override fun getInfoContents(marker: Marker): View? {
                    val info = LinearLayout(this@NearbyDoctorsActivity)
                    info.orientation = LinearLayout.VERTICAL

                    val title = TextView(this@NearbyDoctorsActivity)
                    title.setTextColor(Color.BLACK)
                    title.gravity = Gravity.CENTER
                    title.setTypeface(null, Typeface.BOLD)
                    title.text = marker.title

                    val snippet = TextView(this@NearbyDoctorsActivity)
                    snippet.setTextColor(Color.GRAY)
                    snippet.text = marker.snippet

                    info.addView(title)
                    info.addView(snippet)

                    return info
                }

                override fun getInfoWindow(p0: Marker): View? {
                    return null
                }

            })
        }
    }

    override fun onPause() {
        super.onPause()
        isAgainFetchReq = true
    }

    override fun onResume() {
        super.onResume()
        if(isAgainFetchReq) retrieveUserLocation()
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    private fun checkPermissions(): Boolean {
        if(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ) {
                return true
            }
        return false
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                HelperClass.toast(this,"Permission Granted")
                retrieveUserLocation()
            } else {
                HelperClass.toast(this,"Permission Denied")
            }
        }
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult == null) {
                return
            }
            for (location in locationResult.locations) {
                if (location != null) {
                    retrieveUserLocation()
                }
            }
        }
    }

    private fun requestLocation() {
        val mLocationRequest: LocationRequest = LocationRequest.create()
        mLocationRequest.interval = 6000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        mLocationCallback

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient
            .requestLocationUpdates(mLocationRequest, mLocationCallback, null)

    }

    override fun onDoctorSelected(pos: Int) {
        val intent = Intent(this,ViewDoctorActivity::class.java)
        intent.putExtra(getString(R.string.viewSelectedDoctor),doctorsList[pos])
        startActivity(intent)
    }

}