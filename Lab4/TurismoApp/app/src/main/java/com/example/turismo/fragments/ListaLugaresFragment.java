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
 * ListaLugaresFragment - Muestra lista completa de lugares turísticos
 * Incluye filtros por categoría y búsqueda
 */
public class ListaLugaresFragment extends Fragment {

    // Declaración de variables
    private TextView tvTituloCiudad;
    private ChipGroup chipGroupFiltros;
    private RecyclerView rvLugares;

    private LugarAdapter lugarAdapter;
    private List<Lugar> listaLugares;
    private List<Lugar> listaFiltrada;

    private String categoriaSeleccionada = "Todos";

    /**
     * Constructor estático
     */
    public static ListaLugaresFragment newInstance() {
        return new ListaLugaresFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_lugares, container, false);

        // Inicializar vistas
        inicializarVistas(view);

        // Cargar datos
        cargarLugares();

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
        tvTituloCiudad = view.findViewById(R.id.tvTituloCiudad);
        chipGroupFiltros = view.findViewById(R.id.chipGroupFiltros);
        rvLugares = view.findViewById(R.id.rvListaLugares);

        // Establecer ciudad
        tvTituloCiudad.setText("Arequipa");
    }

    /**
     * Carga la lista de lugares desde BD o API
     */
    private void cargarLugares() {
        listaLugares = new ArrayList<>();

        // Datos de ejemplo
        listaLugares.add(new Lugar(
                "Cañón del Colca",
                "Uno de los cañones más profundos del mundo con impresionantes vistas y avistamiento de cóndores",
                145.00,
                "Aventura, Naturaleza",
                R.drawable.ic_launcher_background
        ));

        listaLugares.add(new Lugar(
                "Monasterio de Santa Catalina",
                "Ciudadela religiosa del siglo XVI con arquitectura colonial única",
                35.00,
                "Aventura, Cultura",
                R.drawable.ic_launcher_background
        ));

        listaLugares.add(new Lugar(
                "Volcán Misti",
                "Imponente volcán de Arequipa, ideal para trekking y aventura con vistas panorámicas",
                145.00,
                "Aventura, Naturaleza",
                R.drawable.ic_launcher_background
        ));

        listaLugares.add(new Lugar(
                "Plaza de Armas",
                "Corazón histórico de la ciudad con imponente catedral de estilo barroco y colonial",
                0.00,
                "Aventura, Cultura",
                R.drawable.ic_launcher_background
        ));

        // Inicialmente mostrar todos
        listaFiltrada = new ArrayList<>(listaLugares);
    }

    /**
     * Configura los chips de filtro
     */
    private void configurarFiltros() {
        String[] categorias = {"Todos", "Cultura", "Naturaleza", "Comida", "Aventura"};

        for (String categoria : categorias) {
            Chip chip = new Chip(getContext());
            chip.setText(categoria);
            chip.setCheckable(true);

            // Primer chip seleccionado por defecto
            if (categoria.equals("Todos")) {
                chip.setChecked(true);
            }

            chip.setOnClickListener(v -> {
                categoriaSeleccionada = categoria;
                filtrarLugares(categoria);
            });

            chipGroupFiltros.addView(chip);
        }

        // Solo un chip seleccionado a la vez
        chipGroupFiltros.setSingleSelection(true);
    }

    /**
     * Configura el RecyclerView
     */
    private void configurarRecyclerView() {
        lugarAdapter = new LugarAdapter(listaFiltrada, lugar -> {
            // Click en item abre detalle
            abrirDetalle(lugar);
        });

        rvLugares.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLugares.setAdapter(lugarAdapter);
    }

    /**
     * Filtra la lista de lugares por categoría
     */
    private void filtrarLugares(String categoria) {
        listaFiltrada.clear();

        if (categoria.equals("Todos")) {
            listaFiltrada.addAll(listaLugares);
        } else {
            for (Lugar lugar : listaLugares) {
                if (lugar.getCategorias().contains(categoria)) {
                    listaFiltrada.add(lugar);
                }
            }
        }

        lugarAdapter.notifyDataSetChanged();
    }

    /**
     * Abre la actividad de detalle
     */
    private void abrirDetalle(Lugar lugar) {
        Intent intent = new Intent(getActivity(), DetalleActivity.class);
        intent.putExtra("LUGAR", lugar);
        startActivity(intent);
    }
}