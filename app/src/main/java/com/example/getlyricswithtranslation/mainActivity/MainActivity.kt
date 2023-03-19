package com.example.getlyricswithtranslation.mainActivity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.getlyricswithtranslation.R
import com.example.getlyricswithtranslation.databinding.ActivityMainBinding
import com.example.getlyricswithtranslation.songInfo.SongInfoActivity
import com.example.getlyricswithtranslation.utils.ListOfSongs
import com.example.getlyricswithtranslation.utils.SongClickListener
import com.example.getlyricswithtranslation.utils.SongsAdapter

class MainActivity : AppCompatActivity() {
    lateinit var searchView:SearchView
    lateinit var mainActivityViewModel: MainActivityViewModel
    lateinit var adapter:SongsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
            R.layout.activity_main
        )
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        binding.mainActivityViewModel = mainActivityViewModel
        binding.lifecycleOwner = this

        adapter= SongsAdapter(
            SongClickListener { id ->
                mainActivityViewModel.navigateToFragmentSongInfo(id)
            })


        mainActivityViewModel.songsList.observe(this, Observer {
            if(it!!.isNotEmpty()) {
                binding.recyclerView1.adapter=adapter
                adapter.submitList(it as MutableList<ListOfSongs>?)
            }else{
                binding.recyclerView1.adapter=null
            }
        })

        mainActivityViewModel.navigateToFragmentSongInfo.observe(this,Observer{
            if(it != null){
                val intent = Intent(application,SongInfoActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra(USER_KEY, it)
                startActivity(intent)
                mainActivityViewModel.doneNavigateToFragmentSongInfo()
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                    if (!searchView.isIconified()) {
                        searchView.onActionViewCollapsed()
                        mainActivityViewModel.changedText()
                    }
                return true
            }
        })

        val searchPlate =  searchView.findViewById(androidx.appcompat.R.id.search_src_text) as? EditText
        searchPlate?.hint = "Search for songs"
        val searchPlateView: View? = searchView.findViewById(androidx.appcompat.R.id.search_plate)
        searchPlateView?.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mainActivityViewModel.loadData(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mainActivityViewModel.changedText()
                return false
            }
        })

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if(!searchView.isIconified()) {
            searchView.onActionViewCollapsed()
        }else{
            super.onBackPressed()
        }
    }
    companion object {
        val USER_KEY = "TransactionPage"
    }

}