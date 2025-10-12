package com.example.turismo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.example.turismo.R;
import com.example.turismo.activities.DetalleActivity;
import com.example.turismo.adapters.LugarAdapter;
import com.example.turismo.models.Lugar;
import java.util.ArrayList;
import java.util.List;

/**
 * FavoritosFragment - Muestra lugares guardados como favoritos
 * Permite filtrar por #Personalizado, #Fecha
 */
public class FavoritosFragment extends Fragment {

    // Declaración de variables
    private ChipGroup chipGroupFiltrosFav;
    private RecyclerView rvFavoritos;
    private TextView tvMensajeVacio;

    private LugarAdapter favoritosAdapter;
    private List<Lugar> listaFavoritos;

    /**
     * Constructor estático
     */
    public static FavoritosFragment newInstance() {
        return new FavoritosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);

        // Inicializar vistas
        inicializarVistas(view);

        // Cargar favoritos
        cargarFavoritos();

        // Configurar filtros
        configurarFiltros();

        // Configurar RecyclerView
        configurarRecyclerView();

        return view;
    }

    /**
     * Inicializa las vistas
     */
    private void inicializarVistas(View view) {
        chipGroupFiltrosFav = view.findViewById(R.id.chipGroupFiltrosFav);
        rvFavoritos = view.findViewById(R.id.rvFavoritos);
        tvMensajeVacio = view.findViewById(R.id.tvMensajeVacio);
    }

    /**
     * Carga los lugares favoritos desde BD local
     */
    private void cargarFavoritos() {
        listaFavoritos = new ArrayList<>();

        // Obtener favoritos de SharedPreferences o BD SQLite
        // Por ahora datos de ejemplo
        listaFavoritos.add(new Lugar(
                "Plaza de Armas",
                "Corazón histórico de la ciudad con imponente catedral",
                0.00,
                "Aventura, Cultura",
                R.drawable.img_plaza
        ));

        listaFavoritos.add(new Lugar(
                "Volcán Misti",
                "Imponente volcán de Arequipa, ideal para trekking",
                145.00,
                "Aventura, Naturaleza",
                R.drawable.img_misti
        ));

        listaFavoritos.add(new Lugar(
                "Cañón del Colca",
                "Uno de los cañones más profundos del mundo",
                145.00,
                "Aventura, Cultura",
                R.drawable.img_colca
        ));

        // Mostrar mensaje si no hay favoritos
        if (listaFavoritos.isEmpty()) {
            tvMensajeVacio.setVisibility(View.VISIBLE);
            rvFavoritos.setVisibility(View.GONE);
        } else {
            tvMensajeVacio.setVisibility(View.GONE);
            rvFavoritos.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Configura los chips de filtro (#Personalizado, #Fecha)
     */
    private void configurarFiltros() {
        String[] filtros = {"#Personalizado", "#Fecha"};

        for (String filtro : filtros) {
            Chip chip = new Chip(getContext());
            chip.setText(filtro);
            chip.setCheckable(true);

            chip.setOnClickListener(v -> {
                // Implementar filtrado
                filtrarFavoritos(filtro);
            });

            chipGroupFiltrosFav.addView(chip);
        }
    }

    /**
     * Configura el RecyclerView de favoritos
     */
    private void configurarRecyclerView() {
        favoritosAdapter = new LugarAdapter(listaFavoritos, lugar -> {
            // Click en item abre detalle
            abrirDetalle(lugar);
        });

        rvFavoritos.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavoritos.setAdapter(favoritosAdapter);
    }

    /**
     * Filtra favoritos según criterio
     */
    private void filtrarFavoritos(String filtro) {
        if (filtro.equals("#Personalizado")) {
            // Ordenar por orden personalizado del usuario
            // Implementar lógica
        } else if (filtro.equals("#Fecha")) {
            // Ordenar por fecha agregado
            // Implementar lógica
        }

        favoritosAdapter.notifyDataSetChanged();
    }

    /**
     * Abre el detalle del lugar
     */
    private void abrirDetalle(Lugar lugar) {
        Intent intent = new Intent(getActivity(), DetalleActivity.class);
        intent.putExtra("LUGAR", lugar);
        startActivity(intent);
    }

    /**
     * Método público para agregar un lugar a favoritos
     */
    public void agregarFavorito(Lugar lugar) {
        if (!listaFavoritos.contains(lugar)) {
            listaFavoritos.add(lugar);
            favoritosAdapter.notifyItemInserted(listaFavoritos.size() - 1);

            // Guardar en BD local
            // guardarEnBD(lugar);

            // Ocultar mensaje vacío
            tvMensajeVacio.setVisibility(View.GONE);
            rvFavoritos.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Elimina un lugar de favoritos
     */
    public void eliminarFavorito(Lugar lugar) {
        int posicion = listaFavoritos.indexOf(lugar);
        if (posicion != -1) {
            listaFavoritos.remove(posicion);
            favoritosAdapter.notifyItemRemoved(posicion);

            // Eliminar de BD
            // eliminarDeBD(lugar);

            // Mostrar mensaje si quedó vacío
            if (listaFavoritos.isEmpty()) {
                tvMensajeVacio.setVisibility(View.VISIBLE);
                rvFavoritos.setVisibility(View.GONE);
            }
        }
    }
}