package com.example.movieapp.service

import android.util.Log
import com.example.movieapp.data.model.ClassToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceBuilder {

    var interceptor : Interceptor = Interceptor { chain ->
        val request = chain.request()
        val builder = request.newBuilder()
        Log.e("TOKEN", ClassToken.MY_TOKEN)
        builder.addHeader("Authorization","Bearer ${ClassToken.MY_TOKEN}")
        return@Interceptor chain.proceed(builder.build())
    }

    var loggingInterceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    var okBuilder : OkHttpClient.Builder = OkHttpClient.Builder()
        .readTimeout(30,TimeUnit.SECONDS)
        .connectTimeout(30,TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .addInterceptor(interceptor)
        .addInterceptor(loggingInterceptor)

    var gson : Gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

    var apiService : ApiService = Retrofit.Builder()
            .baseUrl("https://movie-backend.datisekai.id.vn/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okBuilder.build())
            .build()
            .create(ApiService::class.java)

}