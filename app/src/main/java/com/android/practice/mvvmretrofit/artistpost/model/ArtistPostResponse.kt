package com.android.practice.mvvmretrofit.artistpost.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ArtistPostResponse(
    var artist_post : ArtistPost = ArtistPost()
) : Serializable

data class ArtistPost (

    var artist_thumb : String = "",
    var artist_name  : String= "",
    var post_date    : String= "",
    var post_thumb   : String= "",
    var songs       : ArrayList<Songs> = ArrayList<Songs>()

): Serializable

data class Songs (

    var song_thumb         : String= "",
    var song_name          : String= "",
    var song_likes         : String= "",
    var song_comments      : String= "",
    var song_url      : String= "",
    var song_track_timeline : String= ""

): Serializable