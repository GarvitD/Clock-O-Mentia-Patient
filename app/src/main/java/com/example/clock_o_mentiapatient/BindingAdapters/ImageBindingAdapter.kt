package com.example.clock_o_mentiapatient.BindingAdapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.clock_o_mentiapatient.R
import com.squareup.picasso.Picasso

object ImageBindingAdapter {
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(view: ImageView, url:String){
        Picasso.get().load(url).placeholder(R.drawable.default_profile_img).into(view)
    }
}