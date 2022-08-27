package com.example.clock_o_mentiapatient.Utils

import android.app.Activity
import android.app.AlertDialog
import com.example.clock_o_mentiapatient.R

class ProgressDialogClass(private val activity: Activity) {
    private lateinit var dialog: AlertDialog
    private var instantiated = false

    fun startLoading(){
        if(!instantiated) {
            val inflater = activity.layoutInflater
            val dialogView = inflater.inflate(R.layout.custom_progress_dialog, null)
            val builder = AlertDialog.Builder(activity)
            builder.setView(dialogView)
            builder.setCancelable(false)
            dialog = builder.create()
            dialog.show()
            instantiated = true
        }
    }
    fun dismiss(){
        instantiated = false
        if(this::dialog.isInitialized) dialog.dismiss()
    }
}