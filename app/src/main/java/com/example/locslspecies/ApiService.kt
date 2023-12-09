package com.example.locslspecies

import com.example.locslspecies.model.ApiResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/analyze-url")
    suspend fun getImageInfos(@Query("url") imageUrl: String): ApiResponse

    companion object {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://65.21.143.189:4000/analyze-url/") // Replace with your server URL
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}