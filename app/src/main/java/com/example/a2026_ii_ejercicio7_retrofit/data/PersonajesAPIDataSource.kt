package com.example.a2026_ii_ejercicio7_retrofit.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Interfaz con los endpoints de la API. Retrofit genera la implementación.
interface PersonajesAPIServicio {
    // GET https://rickandmortyapi.com/api/character?page=1
    @GET("character")
    suspend fun getPersonajes(@Query("page") pagina: Int): Response<RespuestaPersonajes>
}

class PersonajesAPIDataSource {

    companion object {
        const val BASE_URL = "https://rickandmortyapi.com/api/"

        // Una sola instancia de Retrofit para toda la app
        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val servicio: PersonajesAPIServicio by lazy {
            retrofit.create(PersonajesAPIServicio::class.java)
        }
    }
}
