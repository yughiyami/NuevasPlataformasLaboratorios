package com.example.turismo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.turismo.R;
import com.example.turismo.models.Lugar;
import java.util.List;

/**
 * LugarAdapter - Adapter para mostrar lista de lugares en RecyclerView
 * Gestiona el binding de datos y eventos de click
 */
public class LugarAdapter extends RecyclerView.Adapter<LugarAdapter.LugarViewHolder> {

    // Lista de lugares
    private List<Lugar> listaLugares;

    // Interface para manejar clicks
    private OnLugarClickListener listener;

    /**
     * Interface para callback de clicks
     */
    public interface OnLugarClickListener {
        void onLugarClick(Lugar lugar);
    }

    /**
     * Constructor
     */
    public LugarAdapter(List<Lugar> listaLugares, OnLugarClickListener listener) {
        this.listaLugares = listaLugares;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LugarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lugar, parent, false);
        return new LugarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LugarViewHolder holder, int position) {
        // Obtener el lugar en la posición actual
        Lugar lugar = listaLugares.get(position);

        // Vincular datos con las vistas
        holder.bind(lugar, listener);
    }

    @Override
    public int getItemCount() {
        return listaLugares != null ? listaLugares.size() : 0;
    }

    /**
     * ViewHolder - Contiene las referencias a las vistas del item
     */
    public static class LugarViewHolder extends RecyclerView.ViewHolder {

        // Vistas del item
        private ImageView ivImagen;
        private TextView tvNombre;
        private TextView tvDescripcion;
        private TextView tvPrecio;
        private TextView tvCategorias;
        private ImageButton btnFavorito;

        /**
         * Constructor del ViewHolder
         */
        public LugarViewHolder(@NonNull View itemView) {
            super(itemView);

            // Inicializar vistas
            ivImagen = itemView.findViewById(R.id.ivImagenLugar);
            tvNombre = itemView.findViewById(R.id.tvNombreLugar);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionLugar);
            tvPrecio = itemView.findViewById(R.id.tvPrecioLugar);
            tvCategorias = itemView.findViewById(R.id.tvCategoriasLugar);
            btnFavorito = itemView.findViewById(R.id.btnFavoritoLugar);
        }

        /**
         * Vincula los datos del lugar con las vistas
         */
        public void bind(Lugar lugar, OnLugarClickListener listener) {
            // Setear datos
            tvNombre.setText(lugar.getNombre());
            tvDescripcion.setText(lugar.getDescripcion());
            tvPrecio.setText(lugar.getPrecioFormateado());
            tvCategorias.setText("Categorías: " + lugar.getCategorias());

            // Cargar imagen (en producción usar Glide o Picasso)
            ivImagen.setImageResource(lugar.getIdImagen());

            // Setear icono de favorito
            if (lugar.isEsFavorito()) {
                btnFavorito.setImageResource(R.drawable.ic_favorite);
            } else {
                btnFavorito.setImageResource(R.drawable.ic_favorite_border);
            }

            // Click en el item completo
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onLugarClick(lugar);
                }
            });

            // Click en botón de favorito
            btnFavorito.setOnClickListener(v -> {
                // Alternar estado de favorito
                lugar.setEsFavorito(!lugar.isEsFavorito());

                // Actualizar icono
                if (lugar.isEsFavorito()) {
                    btnFavorito.setImageResource(R.drawable.ic_favorite);
                } else {
                    btnFavorito.setImageResource(R.drawable.ic_favorite_border);
                }

                // Aquí guardar en BD o SharedPreferences
                // guardarEstadoFavorito(lugar);
            });
        }
    }

    /**
     * Actualiza la lista completa de lugares
     */
    public void actualizarLista(List<Lugar> nuevaLista) {
        this.listaLugares = nuevaLista;
        notifyDataSetChanged();
    }

    /**
     * Agrega un nuevo lugar a la lista
     */
    public void agregarLugar(Lugar lugar) {
        listaLugares.add(lugar);
        notifyItemInserted(listaLugares.size() - 1);
    }

    /**
     * Elimina un lugar de la lista
     */
    public void eliminarLugar(int posicion) {
        listaLugares.remove(posicion);
        notifyItemRemoved(posicion);
    }

    /**
     * Filtra la lista por categoría
     */
    public void filtrar(String categoria) {
        // Implementar lógica de filtrado
        notifyDataSetChanged();
    }
}