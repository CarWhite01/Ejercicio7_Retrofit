package com.example.a2026_ii_ejercicio7_retrofit.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a2026_ii_ejercicio7_retrofit.R
import com.example.a2026_ii_ejercicio7_retrofit.data.Personaje
import com.example.a2026_ii_ejercicio7_retrofit.databinding.ActivityMainBinding
import com.example.a2026_ii_ejercicio7_retrofit.databinding.DialogPersonajeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: PersonajesViewModel by viewModels()
    private val adapter = PersonajesAdapter { personaje -> mostrarDetalle(personaje) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Se inicia el RecyclerView: primero el LayoutManager y después el Adapter
        binding.rvPersonajes.layoutManager = LinearLayoutManager(this)
        binding.rvPersonajes.setHasFixedSize(true)
        binding.rvPersonajes.adapter = adapter

        // Al llegar al final de la lista se pide la siguiente página a la API
        binding.rvPersonajes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && !recyclerView.canScrollVertically(1) && viewModel.hayError.value != true) {
                    viewModel.cargarPersonajes()
                }
            }
        })

        binding.btnReintentar.setOnClickListener { viewModel.cargarPersonajes() }

        // Edge-to-edge: espacio debajo de la lista para la barra de navegación
        ViewCompat.setOnApplyWindowInsetsListener(binding.rvPersonajes) { v, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(bottom = barras.bottom)
            insets
        }

        observarViewModel()
    }

    private fun observarViewModel() {
        // Cada vez que cambia la lista, ListAdapter calcula las diferencias
        viewModel.listaPersonajes.observe(this) { nuevaLista ->
            adapter.submitList(nuevaLista)
            actualizarVistas()
        }

        viewModel.cargando.observe(this) { actualizarVistas() }

        viewModel.hayError.observe(this) { hayError ->
            actualizarVistas()
            // Si ya hay personajes en pantalla, el error de la siguiente página va en un Snackbar
            if (hayError && !viewModel.listaPersonajes.value.isNullOrEmpty()) {
                Snackbar.make(binding.root, R.string.error_siguiente_pagina, Snackbar.LENGTH_LONG)
                    .setAction(R.string.reintentar) { viewModel.cargarPersonajes() }
                    .show()
            }
        }

        viewModel.totalPersonajes.observe(this) { total ->
            binding.toolbar.subtitle = getString(R.string.subtitulo_total, total)
        }
    }

    private fun actualizarVistas() {
        val listaVacia = viewModel.listaPersonajes.value.isNullOrEmpty()
        val cargando = viewModel.cargando.value == true
        val hayError = viewModel.hayError.value == true

        // Círculo grande al centro en la primera carga, barra delgada arriba en las siguientes
        binding.progresoInicial.isVisible = cargando && listaVacia
        binding.progresoPagina.isVisible = cargando && !listaVacia
        binding.grupoError.isVisible = hayError && listaVacia
    }

    private fun mostrarDetalle(personaje: Personaje) {
        val dialogBinding = DialogPersonajeBinding.inflate(LayoutInflater.from(this))
        val estado = EstadoPersonaje.desdeApi(personaje.estado)

        dialogBinding.tvDetalle.text = getString(
            R.string.detalle_personaje,
            getString(estado.texto),
            personaje.especie,
            personaje.genero,
            personaje.origen.nombre,
            personaje.ubicacion.nombre
        )
        dialogBinding.ivFotoGrande.contentDescription = getString(R.string.descripcion_foto, personaje.nombre)

        // La misma URL que en la lista Glide la toma de su caché
        Glide.with(this)
            .load(personaje.imagen)
            .placeholder(R.drawable.ic_imagen)
            .error(R.drawable.ic_error_imagen)
            .centerCrop()
            .into(dialogBinding.ivFotoGrande)

        MaterialAlertDialogBuilder(this)
            .setTitle(personaje.nombre)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.cerrar, null)
            .show()
    }
}
