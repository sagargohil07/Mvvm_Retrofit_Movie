package com.android.practice.mvvmretrofit.movie.api

import com.android.practice.mvvmretrofit.movie.model.ResponseDataModel
import retrofit2.Call
import retrofit2.http.GET


interface MoviesApi {

    @GET("9dab915c-85b9-4ea6-8d6f-33fe992d8ae3")
    fun getMovie() : Call<ResponseDataModel>

}