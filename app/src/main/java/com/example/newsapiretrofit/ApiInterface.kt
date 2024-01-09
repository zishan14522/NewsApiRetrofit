package com.example.newsapiretrofit

import retrofit2.http.GET

interface ApiInterface {
//    @GET("everything")
//    fun getHeadLineData(
//        @Query("country") country:String,
//        @Query("api_key") apikey:String
//    ) : Call<Headlines>

    @GET("/v2/everything?q=pakistan&from=2023-12-09&sortBy=publishedAt&apiKey=8899b2aebb194a9db3793236934f7152")
    suspend fun getNews(
    ) : Headlines

}