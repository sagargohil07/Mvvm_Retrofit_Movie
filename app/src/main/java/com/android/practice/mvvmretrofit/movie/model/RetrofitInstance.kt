package com.android.practice.mvvmretrofit.movie.model

import com.android.practice.mvvmretrofit.movie.api.MoviesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    const val BASE_URL = "https://run.mocky.io/v3/"

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api : MoviesApi = retrofit.create(MoviesApi::class.java)
}