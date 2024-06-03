package com.example.nav.dto

data class UsuarioDTO(
    val id: Int,
    val nombre: String,
    val contraseña: String,
    val correo: String,
    val dni: String,
    val rol: String,
    val telefono: String,
    val direccion: String,
    val userName: String
)
