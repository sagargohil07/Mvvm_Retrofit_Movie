package com.android.practice.mvvmretrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.practice.mvvmretrofit.movie.ViewModel.MoviesViewModel
import com.android.practice.mvvmretrofit.movie.adapter.MoviesAdapter
import com.android.practice.mvvmretrofit.movie.api.OnPlayListener
import com.android.practice.mvvmretrofit.databinding.ActivityMainBinding
import com.android.practice.mvvmretrofit.movie.model.VideoDataModel
import com.google.android.exoplayer2.ExoPlayer

class MainActivity : AppCompatActivity() , OnPlayListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MoviesViewModel

    private var exoPlayer : ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MoviesViewModel::class.java]
        viewModel.getMovies()

        binding.rvMovies.apply {
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.moviesLiveData.observe(this, Observer {
            binding.rvMovies.adapter = MoviesAdapter( this,it as ArrayList<VideoDataModel>,this)
            binding.rvMovies.adapter
        })

        
        /*val intent = Intent(this,Fragment_Container_Activity::class.java)
        startActivity(intent)*/

    }

    private fun loadVideoExoplayer(url:String){
        binding.fragContainer.visibility = View.VISIBLE

        val videoPlayerFragment = VideoPlayerFragment()
        val bundle = Bundle()
        bundle.putString("url",url)
        videoPlayerFragment.arguments=bundle

        supportFragmentManager.beginTransaction().add(R.id.fragContainer,videoPlayerFragment).commit()
    }

    override fun onPlayListener(url: String) {
        loadVideoExoplayer(url)
    }


}