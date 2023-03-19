package com.example.getlyricswithtranslation.data

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData


import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

//
//
//class SongInfo(val id:String?) : AsyncTask<URL?, Int?, String?>() {
//    private var getSong=MutableLiveData<songInfo?>()
//    override fun doInBackground(vararg p0: URL?): String? {
//        val urlString = "http://api.genius.com/songs/$id"
//        val url = URL(urlString)
//        val connection = url.openConnection() as HttpURLConnection
//        connection.requestMethod = "GET"
//        connection.setRequestProperty("Authorization", "Bearer $accessToken")
//        val inputStream = connection.inputStream
//        val response = BufferedReader(InputStreamReader(inputStream)).use(BufferedReader::readText)
//        return response
//    }
//
//    override fun onProgressUpdate(vararg values: Int?) {
//
//    }
//
//    override fun onPostExecute(result: String?) {
//        Log.d("Priya", result.toString())
//    }
//}
