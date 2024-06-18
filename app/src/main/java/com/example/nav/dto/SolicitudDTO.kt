package com.example.nav.dto

data class SolicitudDTO (
    var id: Int? = null,
    var fechaSolicitud: String? = null,
    var descripcion: String? = null,
    var atendida: Boolean? = null,
    var idUsuario: Int? = null
)