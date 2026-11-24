package com.example.a2026_ii_ejercicio7_retrofit.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2026_ii_ejercicio7_retrofit.data.Personaje
import com.example.a2026_ii_ejercicio7_retrofit.data.PersonajesRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class PersonajesViewModel : ViewModel() {

    private val repositorio = PersonajesRepository()

    private val _listaPersonajes = MutableLiveData<List<Personaje>>(emptyList())
    val listaPersonajes: LiveData<List<Personaje>> get() = _listaPersonajes

    private val _cargando = MutableLiveData(false)
    val cargando: LiveData<Boolean> get() = _cargando

    private val _hayError = MutableLiveData(false)
    val hayError: LiveData<Boolean> get() = _hayError

    private val _totalPersonajes = MutableLiveData<Int>()
    val totalPersonajes: LiveData<Int> get() = _totalPersonajes

    // null cuando ya no hay más páginas que pedir
    private var siguientePagina: Int? = 1

    init {
        // La primera página se pide una sola vez; al girar la pantalla
        // el ViewModel sobrevive y no se vuelve a llamar a la API.
        cargarPersonajes()
    }

    fun cargarPersonajes() {
        val pagina = siguientePagina ?: return
        if (_cargando.value == true) return

        _cargando.value = true
        _hayError.value = false

        // viewModelScope cancela la corrutina si el ViewModel se destruye.
        // Las funciones suspend de Retrofit ya hacen la petición fuera del hilo principal.
        viewModelScope.launch {
            try {
                val respuesta = repositorio.getPersonajes(pagina)
                _listaPersonajes.value = _listaPersonajes.value.orEmpty() + respuesta.personajes
                _totalPersonajes.value = respuesta.info.total
                siguientePagina = if (respuesta.info.siguiente != null) pagina + 1 else null
            } catch (err: CancellationException) {
                throw err
            } catch (err: Exception) {
                Log.e("ERROR_RETROFIT", "Error retrofit: " + err.message)
                _hayError.value = true
            } finally {
                _cargando.value = false
            }
        }
    }

    fun hayMasPaginas(): Boolean = siguientePagina != null
}
