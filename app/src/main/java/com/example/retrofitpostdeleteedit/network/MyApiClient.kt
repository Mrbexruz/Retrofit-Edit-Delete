package com.example.retrofitpostdeleteedit.network

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MyApiClient {

    const val BASE_URL ="https://todoeasy.pythonanywhere.com/ "

    fun getApiService():ApiService {
        return getRetrofit().create(ApiService::class.java)
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}