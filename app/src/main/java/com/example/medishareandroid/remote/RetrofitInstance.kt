//remote/RetrofitInstance.kt
package com.example.medishareandroid.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



// 10.0.2.2 adresse locale de l'émulateur

const val BASE_URL = "http://192.168.137.1:3000/"
//const val BASE_URL = "http://192.168.1.156:30000/auth/"


abstract class RetrofitInstance {

    companion object {
        fun getRetrofit(): Retrofit {
            return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
//        .client(OkHttpClient().newBuilder().addInterceptor())  // pour ajouter un token
                .build()
        }
    }
}
