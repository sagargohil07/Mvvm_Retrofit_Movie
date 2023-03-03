package com.android.practice.mvvmretrofit.movie.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.practice.mvvmretrofit.movie.model.ResponseDataModel
import com.android.practice.mvvmretrofit.movie.model.RetrofitInstance
import com.android.practice.mvvmretrofit.movie.model.VideoDataModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesViewModel : ViewModel() {

     var moviesLiveData = MutableLiveData<List<VideoDataModel>>()

    fun getMovies(){
        RetrofitInstance.api.getMovie().enqueue(object : Callback<ResponseDataModel?> {
            override fun onResponse(call: Call<ResponseDataModel?>, response: Response<ResponseDataModel?>) {
                val result = response.body()
                if (result!==null){
                    Log.d("getmovie", "onResponse: $result")
                    moviesLiveData.postValue(response.body()!!.videos)
                }
            }

            override fun onFailure(call: Call<ResponseDataModel?>, t: Throwable) {
                Log.d("getmovie", "onFailure: ${t.message}")
            }
        })
    }

}


