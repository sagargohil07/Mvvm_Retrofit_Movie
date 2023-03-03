package com.android.practice.mvvmretrofit

import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.android.practice.mvvmretrofit.databinding.FragmentVideoPlayerBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

class VideoPlayerFragment : Fragment() {

    lateinit var binding : FragmentVideoPlayerBinding

    private var exoPlayer : ExoPlayer? = null

    private lateinit var url : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        url = bundle?.getString("url").toString()

        Log.d("TAG", "onViewCreated: ${arguments.toString()}")
        initPlayer()

        binding.ivBack.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentVideoPlayerBinding.inflate(inflater,container,false)

        return binding.root
    }

    private fun initPlayer(){
        val mediaDataSourceFactory = DefaultDataSource.Factory(requireContext())

        val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(MediaItem.fromUri(url))

        val mediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)

        exoPlayer = ExoPlayer.Builder(requireContext()).setMediaSourceFactory(mediaSourceFactory).build()

        exoPlayer!!.addMediaSource(mediaSource)

        exoPlayer!!.playWhenReady = true
        exoPlayer!!.playbackState
        binding.playerView.player = exoPlayer!!

        binding.playerView.requestFocus()
        exoPlayer!!.prepare()

    }

    private fun releasePlayer(){
        exoPlayer!!.release()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

}


