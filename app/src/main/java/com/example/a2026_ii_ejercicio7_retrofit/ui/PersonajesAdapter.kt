package com.example.a2026_ii_ejercicio7_retrofit.ui

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.a2026_ii_ejercicio7_retrofit.R
import com.example.a2026_ii_ejercicio7_retrofit.data.Personaje
import com.example.a2026_ii_ejercicio7_retrofit.databinding.ItemPersonajeBinding

// ListAdapter + DiffUtil: al llegar una página nueva solo se dibujan los elementos nuevos
class PersonajesAdapter(
    private val onClick: (Personaje) -> Unit
) : ListAdapter<Personaje, PersonajesAdapter.PersonajeHolder>(PersonajeDiffCallback) {

    class PersonajeHolder(
        private val itemBinding: ItemPersonajeBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun llenarItemView(personaje: Personaje) {
            val context = itemBinding.root.context
            val estado = EstadoPersonaje.desdeApi(personaje.estado)

            itemBinding.tvNombre.text = personaje.nombre
            itemBinding.tvEstado.text = context.getString(
                R.string.formato_estado_especie, context.getString(estado.texto), personaje.especie
            )
            itemBinding.vEstado.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, estado.color))
            itemBinding.tvUbicacion.text = personaje.ubicacion.nombre
            itemBinding.ivFoto.contentDescription =
                context.getString(R.string.descripcion_foto, personaje.nombre)

            // Glide descarga la imagen, la guarda en caché y la pone en el ImageView.
            // placeholder se ve mientras carga. error: se ve si falla la descarga.
            Glide.with(itemBinding.ivFoto)
                .load(personaje.imagen)
                .placeholder(R.drawable.ic_imagen)
                .error(R.drawable.ic_error_imagen)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemBinding.ivFoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonajeHolder {
        val itemBinding = ItemPersonajeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = PersonajeHolder(itemBinding)
        // El listener se asigna una sola vez y la posición se lee al momento del clic
        itemBinding.root.setOnClickListener {
            val posicion = holder.bindingAdapterPosition
            if (posicion != RecyclerView.NO_POSITION) {
                onClick(getItem(posicion))
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: PersonajeHolder, position: Int) {
        holder.llenarItemView(getItem(position))
    }

    object PersonajeDiffCallback : DiffUtil.ItemCallback<Personaje>() {
        override fun areItemsTheSame(oldItem: Personaje, newItem: Personaje) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Personaje, newItem: Personaje) = oldItem == newItem
    }
}
