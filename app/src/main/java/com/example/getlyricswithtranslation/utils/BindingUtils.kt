package com.example.getlyricswithtranslation.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let{
        Glide.with(imgView.context).load(imgUrl).into(imgView)
    }
}