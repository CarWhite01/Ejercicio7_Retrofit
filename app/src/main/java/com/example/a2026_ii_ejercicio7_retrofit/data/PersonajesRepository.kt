package com.example.a2026_ii_ejercicio7_retrofit.data

import java.io.IOException

// El repositorio es el único que habla con la fuente de datos la API
// El ViewModel le pide los datos y no sabe que vienen de Retrofit.
class PersonajesRepository {

    suspend fun getPersonajes(pagina: Int): RespuestaPersonajes {
        val response = PersonajesAPIDataSource.servicio.getPersonajes(pagina)

        if (response.isSuccessful) {
            return response.body() ?: throw IOException("La respuesta llegó vacía")
        } else {
            // Se lanza el error para que el ViewModel lo muestre en la vista
            throw IOException("Error HTTP ${response.code()}")
        }
    }
}
