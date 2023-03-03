package com.android.practice.mvvmretrofit.artistpost.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.practice.mvvmretrofit.R
import com.android.practice.mvvmretrofit.artistpost.api.OnItemSelectListener
import com.android.practice.mvvmretrofit.artistpost.model.Songs
import com.android.practice.mvvmretrofit.databinding.RawSongListBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class SongAdapter(private var context: Context,private val songlist: ArrayList<Songs> , private var onItemSelectListener: OnItemSelectListener)
    : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    inner class SongViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val binding = RawSongListBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.raw_song_list,parent,false)
        return SongViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val itemModel = songlist[position]
        Glide.with(context)
            .load(itemModel.song_thumb)
            .into(holder.binding.ivThumb)

        holder.binding.tvName.text = itemModel.song_name
        holder.binding.tvTimeLine.text = itemModel.song_track_timeline

        holder.itemView.setOnClickListener {
            onItemSelectListener.onItemSelected(position)
        }
    }

    override fun getItemCount(): Int {
        return songlist.size
    }
}