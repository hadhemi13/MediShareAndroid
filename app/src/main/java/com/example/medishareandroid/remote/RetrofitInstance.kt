//remote/RetrofitInstance.kt
package com.example.medishareandroid.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



// 10.0.2.2 adresse locale de l'émulateur
const val BASE_URL = "http://172.18.21.224:3000/auth/"
//const val BASE_URL = "http://192.168.1.156:3000/auth/"

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
