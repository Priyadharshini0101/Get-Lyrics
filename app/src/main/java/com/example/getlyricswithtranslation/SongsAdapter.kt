package com.example.getlyricswithtranslation.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.getlyricswithtranslation.databinding.ListItemsBinding

class SongsAdapter(val clickListener: SongClickListener): ListAdapter<ListOfSongs, SongsAdapter.ViewHolder>(DiffCallback()) {

       val itemList = mutableListOf<ListOfSongs>()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun submitList(list: MutableList<ListOfSongs>?) {
        super.submitList(list)
        itemList.clear()
        itemList.addAll(list!!)
        notifyDataSetChanged()
    }

    class ViewHolder private constructor(val binding: ListItemsBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ListOfSongs,clickListener: SongClickListener) {
            binding.data = item
            binding.songClickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class SongClickListener(val clickListener: (song:ListOfSongs) -> Unit){
    fun onClick(song: ListOfSongs)=clickListener(song)
}

class DiffCallback : DiffUtil.ItemCallback<ListOfSongs>() {
    override fun areItemsTheSame(oldItem: ListOfSongs, newItem: ListOfSongs): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem:ListOfSongs, newItem:ListOfSongs): Boolean {
        return oldItem== newItem
    }
}