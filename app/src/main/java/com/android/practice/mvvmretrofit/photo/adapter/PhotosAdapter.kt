package com.android.practice.mvvmretrofit.photo.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.practice.mvvmretrofit.R
import com.android.practice.mvvmretrofit.databinding.RawImageListBinding
import com.android.practice.mvvmretrofit.photo.model.PhotoModel
import com.bumptech.glide.Glide

class PhotosAdapter(private val context: Context,private val photoList : ArrayList<PhotoModel>) : RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>(){

    inner class PhotoViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding = RawImageListBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view  = LayoutInflater.from(parent.context).inflate(R.layout.raw_image_list,parent,false)
        return  PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {

        Log.d("photo", "onBindViewHolder: photoList[position].image : ${photoList[position].image}")

        Glide.with(context)
            .load(photoList[position].image)
            .into(holder.binding.ivPhoto)
    }

    override fun getItemCount(): Int {
      return photoList.size
    }
}