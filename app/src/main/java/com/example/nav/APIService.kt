package com.example.prueba

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {
    @GET("/api/v2/pokemon/{name}")
    suspend fun getFiles(@Path("name") name:String): Response<User>

}