package com.example.getlyricswithtranslation.songInfo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.getlyricswithtranslation.R
import com.example.getlyricswithtranslation.databinding.ActivityMainBinding
import com.example.getlyricswithtranslation.databinding.SongInfoBinding
import com.example.getlyricswithtranslation.mainActivity.MainActivity
import com.example.getlyricswithtranslation.utils.ListOfSongs

class SongInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val listOfSongs: ListOfSongs?
        val binding = DataBindingUtil.setContentView<SongInfoBinding>(this, R.layout.song_info)
        listOfSongs = intent.getParcelableExtra<ListOfSongs>(MainActivity.USER_KEY)
        title = listOfSongs!!.title
        val viewModelFactory = SongInfoViewModelFactory(listOfSongs)

        val songInfoViewModel = ViewModelProvider(this, viewModelFactory).get(SongInfoViewModel::class.java)
        binding.songInfoViewModel = songInfoViewModel
        binding.lifecycleOwner = this
        songInfoViewModel.loadData(listOfSongs)
        songInfoViewModel.loadLyrics(0)

        songInfoViewModel.songInfo.observe(this, Observer {
            if(it != null){
                Log.d("Priya",it.toString())
            }
        })

    }
}