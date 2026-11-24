package com.example.a2026_ii_ejercicio7_retrofit.data

import com.google.gson.annotations.SerializedName

// Modelos que Gson llena a partir del JSON de
// https://rickandmortyapi.com/api/character?page=1
// @SerializedName indica el nombre del campo en el JSON.

data class RespuestaPersonajes(
    @SerializedName("info")
    val info: InfoPaginas,
    @SerializedName("results")
    val personajes: List<Personaje>
)

data class InfoPaginas(
    @SerializedName("count")
    val total: Int,
    @SerializedName("pages")
    val paginas: Int,
    @SerializedName("next")
    val siguiente: String?
)

data class Personaje(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val nombre: String,
    @SerializedName("status")
    val estado: String,
    @SerializedName("species")
    val especie: String,
    @SerializedName("gender")
    val genero: String,
    @SerializedName("origin")
    val origen: Lugar,
    @SerializedName("location")
    val ubicacion: Lugar,
    @SerializedName("image")
    val imagen: String
)

data class Lugar(
    @SerializedName("name")
    val nombre: String
)
