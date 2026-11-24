package com.example.a2026_ii_ejercicio7_retrofit.ui

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.example.a2026_ii_ejercicio7_retrofit.R

// Traduce el "status" que manda la API (en inglés) a un texto y un color de la app.
enum class EstadoPersonaje(@StringRes val texto: Int, @ColorRes val color: Int) {
    VIVO(R.string.estado_vivo, R.color.estado_vivo),
    MUERTO(R.string.estado_muerto, R.color.estado_muerto),
    DESCONOCIDO(R.string.estado_desconocido, R.color.estado_desconocido);

    companion object {
        fun desdeApi(status: String): EstadoPersonaje = when (status.lowercase()) {
            "alive" -> VIVO
            "dead" -> MUERTO
            else -> DESCONOCIDO
        }
    }
}
