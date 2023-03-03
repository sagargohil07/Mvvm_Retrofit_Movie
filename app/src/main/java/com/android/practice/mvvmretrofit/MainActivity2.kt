package com.android.practice.mvvmretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.practice.mvvmretrofit.databinding.ActivityMain2Binding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource


class MainActivity2 : AppCompatActivity() {

    private var exoPlayer: ExoPlayer? = null

    private lateinit var binding : ActivityMain2Binding

    private lateinit var url : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        url = intent?.extras?.getString("url").toString()
        Log.d("exo", "onCreate: $url")

        initializePlayer()
    }

    private fun initializePlayer() {

        //data fetching from content URL/local data file and extract the data from it.
        val mediaDataSourceFactory = DefaultDataSource.Factory(this)//DefaultData Support multiple url schema

        //load extracted data from media datasource-factory and define time line
        // 1.Factory(create new factory for file formats mp4 mkv )
        // 2.creating media source using current parameter
        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(MediaItem.fromUri(url))

        //ensure read data from url in exoplayer
        val mediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)

        // creating exoplayer and setting media factory ( which read data from url )
        exoPlayer = ExoPlayer.Builder(this).setMediaSourceFactory(mediaSourceFactory).build()

        // now adding extracted data in media source
        exoPlayer!!.addMediaSource(mediaSource)

        // paush and resume playback
        exoPlayer!!.playWhenReady = true

        //setting player to the view
        binding.playerView.player = exoPlayer!!

        //setting focus on exoplayer view
        binding.playerView.requestFocus()

    }

    private fun releasePlayer() {
        //released when player is not needed anymore
        exoPlayer!!.release()
    }

    public override fun onResume() {
        super.onResume()
         initializePlayer()
    }

    public override fun onPause() {
        super.onPause()
         releasePlayer()
    }

    public override fun onStop() {
        super.onStop()
         releasePlayer()
    }

}