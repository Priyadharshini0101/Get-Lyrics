package com.example.getlyricswithtranslation.mainActivity

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.getlyricswithtranslation.utils.Constants
import com.example.getlyricswithtranslation.utils.ListOfSongs
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

val accessToken = "7s8KjhtKOelSYb0mtSkZCNJiuyR9nZjr2LlnlrSaQFEvsPoIG4IPw25By9s8ByXl"

class MainActivityViewModel: ViewModel() {

    private val _songsList = MutableLiveData<List<ListOfSongs>?>()
    val songsList: LiveData<List<ListOfSongs>?>
        get() = _songsList

    private val _navigateToFragmentSongInfo = MutableLiveData<ListOfSongs?>()
    val navigateToFragmentSongInfo: LiveData<ListOfSongs?>
        get() = _navigateToFragmentSongInfo

    fun changedText() {
        _songsList.value = emptyList()
    }

    fun navigateToFragmentSongInfo(id:ListOfSongs){
        _navigateToFragmentSongInfo.value = id
    }

    fun doneNavigateToFragmentSongInfo(){
        _navigateToFragmentSongInfo.value = null
    }

   fun loadData(data:String?) {

    class songs() : AsyncTask<URL?, Int?, String?>() {

        override fun doInBackground(vararg p0: URL?): String? {
            val urlString = "https://api.genius.com/search?q=$data"
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Authorization", "Bearer ${Constants.accessToken}")
            val inputStream = connection.inputStream
            val response =
                BufferedReader(InputStreamReader(inputStream)).use(BufferedReader::readText)
            return response
        }

        override fun onProgressUpdate(vararg values: Int?) {

        }

        override fun onPostExecute(result: String?) {
            val arrayList = ArrayList<ListOfSongs>()
            val response = JSONObject(result.toString()).getJSONObject("response")
            val hits = response.getJSONArray("hits")
            val size = hits.length()

            for (i in 1 until size) {
                val results = hits.getJSONObject(i)
                val eachSong = results.getJSONObject("result")


                val song = ListOfSongs(
                    eachSong.getString("api_path"),
                    eachSong.getString("artist_names"),
                    eachSong.getString("header_image_thumbnail_url") ,
                    eachSong.getString("header_image_url"),
                    eachSong.getString("id"),
                    eachSong.getString("language"),
                    eachSong.getString("release_date_for_display"),
                    eachSong.getString("title")
                )
                arrayList.add(song)
            }
            _songsList.postValue(arrayList)
        }
    }
       songs().execute()


   }
}

