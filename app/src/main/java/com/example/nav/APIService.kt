package com.example.prueba

import com.example.nav.data.LoginRequest
import com.example.nav.data.NameRequest
import com.example.nav.dto.ImageneDTO
import com.example.nav.dto.UsuarioDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIService {
    @POST("/imagenes/cliente")
    suspend fun getFiles(@Body usuarioDTO: UsuarioDTO?): Response<List<ImageneDTO>>

    @GET("/imagenes/invitado")
    suspend fun getImagesGuest(): Response<List<ImageneDTO>>

    // Define la interfaz para las solicitudes HTTP
    @POST("/usuarios/login")
    fun login(@Body requestBody: LoginRequest): Call<UsuarioDTO>

    @POST("/usuarios")
    fun register(@Body usuarioDTO: UsuarioDTO?): Call<UsuarioDTO>


}