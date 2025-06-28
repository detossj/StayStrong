package com.deto.staystrong.data.remote

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    //private const val BASE_URL = "http://10.0.2.2:8000"
    const val BASE_URL = "http://192.168.1.91:8000"

    fun create(context: Context): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(client)
            .baseUrl(BASE_URL)
            .build()

    }
}