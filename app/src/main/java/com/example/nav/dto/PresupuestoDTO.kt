package com.example.nav.dto

data class PresupuestoDTO(
    val id: Int,
    val fechalEnvio: String,
    val estado: String,
    val idJardin: Int,
    val idSolicitud: Int,
    val ubicacion: String
)