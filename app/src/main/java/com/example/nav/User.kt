package com.example.prueba

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("weight") val weight: Int,
    @SerializedName("order") val order: Int,
    @SerializedName("name") val name: String,
    @SerializedName("sprites") val image: pokemonSprites
)

data class pokemonSprites(
    @SerializedName("front_shiny") val urlShiny: String,
    @SerializedName("front_default") val url: String
)