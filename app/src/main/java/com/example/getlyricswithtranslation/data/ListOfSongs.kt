package com.example.getlyricswithtranslation.utils

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.InputStream

@Parcelize
data class ListOfSongs(
    val api_path:String="",
    val artist_names:String="",
    val header_image_thumbnail_url:String="",
    val header_image_url:String="",
    val id:String="",
    val language:String ="",
    val release_date_for_display:String="",
    val title:String="",
): Parcelable

data class SongInfo(
    val song:ListOfSongs,
    val name:String="",
    val paths:ArrayList<String> = arrayListOf(),
    val music_by_name:String="",
    val label_name:String="",
    val url:String="",
    var lyrics:String = "",
)
