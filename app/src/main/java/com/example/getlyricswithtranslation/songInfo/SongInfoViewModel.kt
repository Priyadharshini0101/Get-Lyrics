package com.example.getlyricswithtranslation.songInfo

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.getlyricswithtranslation.utils.Constants
import com.example.getlyricswithtranslation.utils.ListOfSongs
import com.example.getlyricswithtranslation.utils.SongInfo
import org.json.JSONObject
import java.io.BufferedReader
import java.io.EOFException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


class SongInfoViewModel(song: ListOfSongs): ViewModel() {
    private val _songInfo = MutableLiveData<String?>()
    val songInfo: LiveData<String?>
        get() = _songInfo
    val path: ArrayList<String> = arrayListOf()
    lateinit var songDet:SongInfo


    fun loadData(song:ListOfSongs) {

        class songIn() : AsyncTask<URL?, Int?, String?>() {

            override fun doInBackground(vararg p0: URL?): String? {
                val urlString = "https://api.genius.com/songs/${song.id}"
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Authorization", "Bearer ${Constants.accessToken}")
                val inputStream = connection.inputStream
                val response = BufferedReader(InputStreamReader(inputStream)).use(BufferedReader::readText)
                return response
            }

            override fun onProgressUpdate(vararg values: Int?) {

            }

            override fun onPostExecute(result: String?) {

                val response = JSONObject(result.toString()).getJSONObject("response").getJSONObject("song")
                val name = response.getJSONObject("album").getString("name")
                val lyrics = response.getString("path")
                val englishLyrics = response.getJSONArray("translation_songs").getJSONObject(0).getString("path")
                val romanizedLyrics = response.getJSONArray("translation_songs").getJSONObject(1).getString("path")
                val getMusicByName = response.getJSONArray("custom_performances").getJSONObject(1)
                val getLabelName = response.getJSONArray("custom_performances").getJSONObject(2)
                var musicByName= ""
                var labelName = ""
                if(!getMusicByName.isNull("artists") && !getLabelName.isNull("artists")){
                     musicByName = getMusicByName.getJSONArray("artists").getJSONObject(0).getString("name")
                    labelName = getLabelName.getJSONArray("artists").getJSONObject(0).getString("name")
                }
                val url = response.getJSONArray("media").getJSONObject(0).getString("url")

                path.add(lyrics)
                path.add(englishLyrics)
                path.add(romanizedLyrics)

                songDet = SongInfo(song,name, path, musicByName, labelName, url)


            }
        }
        songIn().execute()
    }

    fun loadLyrics(index:Int) {
        class getLyrics() : AsyncTask<URL?, Int?, String?>() {
            override fun doInBackground(vararg p0: URL?): String? {
                val urlString = "https://genius.com/Arijit-singh-and-shreya-ghoshal-samjhawan-lyrics"
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty("Authorization", "Bearer ${Constants.accessToken}")
                var lyrics:String? = ""
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                    val stringBuilder = StringBuilder()

                    var line: String?
                    while (bufferedReader.readLine().also { line = it } != "</html>") {
                        stringBuilder.append(line)
                    }

                    bufferedReader.close()
                    inputStream.close()

                    val html = stringBuilder.toString()
                    val lyricsStart =
                        html.indexOf("<div class=\"Lyrics__Container-sc-1ynbvzw-6\">")
                    val lyricsEnd = html.indexOf("</div>", lyricsStart)
                    lyrics = html.substring(lyricsStart,lyricsEnd)


                } else {
                    println("HTTP error code: $responseCode")
                }

                connection.disconnect()

                return lyrics
            }

            override fun onProgressUpdate(vararg values: Int?) {

            }

            override fun onPostExecute(result: String?) {
             songDet.lyrics = result!!
                _songInfo.postValue(result)

            }
        }
        getLyrics().execute()


    }
}

