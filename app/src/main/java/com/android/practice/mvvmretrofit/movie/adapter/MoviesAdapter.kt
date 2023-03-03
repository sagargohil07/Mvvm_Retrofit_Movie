package com.android.practice.mvvmretrofit.movie.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.practice.mvvmretrofit.MainActivity
import com.android.practice.mvvmretrofit.R
import com.android.practice.mvvmretrofit.VideoPlayerFragment
import com.android.practice.mvvmretrofit.movie.api.OnPlayListener
import com.android.practice.mvvmretrofit.movie.model.VideoDataModel
import com.android.practice.mvvmretrofit.databinding.RawVideosListBinding
import com.bumptech.glide.Glide

class MoviesAdapter(private val context: Context, private var moviesList : ArrayList<VideoDataModel>, private val onPlayListener: OnPlayListener)
    : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    inner class MoviesViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val binding = RawVideosListBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.raw_videos_list,parent,false)
        return MoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val itemModel = moviesList[position]

        val url  = itemModel.videoPath.replaceAfter("sample/","")
        Glide.with(holder.itemView).load(url +itemModel.image).into(holder.binding.ivMovieThumb)
        Log.d("url", "onBindViewHolder:  ${url +itemModel.image}")

        holder.binding.tvMovieName.text = itemModel.title
        holder.binding.tvMovieDescription.text = itemModel.description
        holder.binding.tvMovieBy.text = itemModel.sources

        holder.binding.btnPlay.setOnClickListener {
            //itemModel.videoPath
            onPlayListener.onPlayListener(itemModel.videoPath)
        }
    }


    override fun getItemCount(): Int {
        return moviesList.size
    }

}