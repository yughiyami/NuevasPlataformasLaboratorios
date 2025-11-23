package com.example.laboratorio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EdificacionAdapter(
    private val listaOriginal: List<Edificacion>
) : RecyclerView.Adapter<EdificacionAdapter.EdificacionViewHolder>(), Filterable {

    private var listaFiltrada: List<Edificacion> = listaOriginal

    inner class EdificacionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivEdificacion: ImageView = view.findViewById(R.id.ivEdificacion)
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvCategoria: TextView = view.findViewById(R.id.tvCategoria)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EdificacionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_edificacion, parent, false)
        return EdificacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: EdificacionViewHolder, position: Int) {
        val edificacion = listaFiltrada[position]

        holder.ivEdificacion.setImageResource(edificacion.imagenResId)
        holder.tvNombre.text = edificacion.nombre
        holder.tvCategoria.text = edificacion.categoria
        holder.tvDescripcion.text = edificacion.descripcion
    }

    override fun getItemCount(): Int = listaFiltrada.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filtro = constraint?.toString()?.lowercase()?.trim() ?: ""

                listaFiltrada = if (filtro.isEmpty()) {
                    listaOriginal
                } else {
                    listaOriginal.filter { edificacion ->
                        edificacion.nombre.lowercase().contains(filtro) ||
                                edificacion.categoria.lowercase().contains(filtro) ||
                                edificacion.descripcion.lowercase().contains(filtro)
                    }
                }

                return FilterResults().apply {
                    values = listaFiltrada
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()
            }
        }
    }
}
