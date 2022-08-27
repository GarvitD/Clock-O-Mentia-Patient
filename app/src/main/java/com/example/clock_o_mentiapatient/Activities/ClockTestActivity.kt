package com.example.clock_o_mentiapatient.Activities

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.clock_o_mentiapatient.BuildConfig
import com.example.clock_o_mentiapatient.models.NetworkState
import com.example.clock_o_mentiapatient.R
import com.example.clock_o_mentiapatient.Utils.HelperClass
import com.example.clock_o_mentiapatient.Utils.ProgressDialogClass
import com.example.clock_o_mentiapatient.Utils.ResponseCode
import com.example.clock_o_mentiapatient.ViewModels.ClockTestViewModel
import com.example.clock_o_mentiapatient.databinding.ActivityClockTestBinding
import com.swein.easypermissionmanager.EasyPermissionManager
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class ClockTestActivity : AppCompatActivity() {

    private lateinit var activityClockTestBinding: ActivityClockTestBinding
    private var latestTmpUri: Uri? = null
    private val clockTestViewModel by viewModels<ClockTestViewModel>()
    private val progressDialog = ProgressDialogClass(this)

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let {
                    activityClockTestBinding.imagePreview.setImageURI(it)
                    uriToFile(this, it, "select_image_from_camera")?.let { file ->
                        Log.e("photoUpload", "reached here1")

                        sendImage(file)
                    }
                }
            }
        }

    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            Log.e("photoUpload", "reached here1")
            uri?.let {
                activityClockTestBinding.imagePreview.setImageURI(it)

                uriToFile(this, it, "select_image_from_gallery")?.let { file ->
                    sendImage(file)
                }

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityClockTestBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_clock_test)

        EasyPermissionManager(this).requestPermission(
            "permission",
            "permission are necessary",
            "setting",
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )

        activityClockTestBinding.takeImageButton.setOnClickListener {
            takeImage()
        }

        activityClockTestBinding.selectImageButton.setOnClickListener {
            selectImageFromGallery()
        }

        initObservers()
    }

    private fun initObservers() {
        clockTestViewModel.clockTestResult.observe(this) { response ->
            fun handleLoading() {
                progressDialog.startLoading()
            }

            fun handleSuccess() {
                progressDialog.dismiss()

                if (response.code == ResponseCode.CODE_200) {

                    progressDialog.dismiss()
                    if (response.data != null){
                        Log.e("photoUpload",response.data.toString())

                        response.data?.let {
                            Log.e("photoUpload","${it.prediction}\n${it.probabilities}\n${it.status}")
                        }
                    }
                    else{
                        Log.e("photoUpload", "null result")
                    }
                }

            }

            fun handleError() {
                progressDialog.dismiss()
                HelperClass.toast(this, response.message)
            }

            when (response.networkState) {
                NetworkState.LOADING -> handleLoading()
                NetworkState.SUCCESS -> handleSuccess()
                NetworkState.ERROR -> handleError()
            }

        }
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
        getTmpFileUri().let { uri ->
            latestTmpUri = uri
            takeImageResult.launch(uri)
             }
        }
    }

    private fun createImageFile(fileName: String = "temp_image"): File {
        return File.createTempFile(
            fileName,
            ".jpeg",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
    }

    private fun uriToFile(context: Context, uri: Uri, fileName: String): File? {
         context.contentResolver.openInputStream(uri)?.let {
              val tempFile = createImageFile(fileName)
              val fileOutputStream = FileOutputStream(tempFile)
             it.copyTo(fileOutputStream)
             it.close()
             fileOutputStream.close()
             return tempFile
         }
         return null
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            applicationContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    private fun sendImage(file: File) {

        Log.e("photoUpload", "reached here2")
        val requestFile: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        clockTestViewModel.getClockTestResult(body)

    }

}