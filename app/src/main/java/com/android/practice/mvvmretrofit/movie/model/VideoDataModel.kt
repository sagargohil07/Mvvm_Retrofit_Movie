package com.android.practice.mvvmretrofit.movie.model

import com.google.gson.annotations.SerializedName

data class VideoDataModel(

     //@SerializedName() used make custom name for data class variable and sync response key to data class variable name
     @SerializedName("thumb")
     val image : String,

     @SerializedName("sources")
     val videoPath : String,

     val title : String,

     val description : String,

     @SerializedName("subtitle")
     val sources : String

)
