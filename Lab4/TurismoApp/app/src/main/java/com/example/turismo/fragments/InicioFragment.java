package com.example.turismo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.turismo.R;
import com.example.turismo.activities.DetalleActivity;
import com.example.turismo.adapters.LugarAdapter;
import com.example.turismo.models.Lugar;
import java.util.ArrayList;
import java.util.List;

/**
 * InicioFragment - Pantalla principal de bienvenida
 * Muestra búsqueda, carrusel de imágenes y recomendaciones
 */
public class InicioFragment extends Fragment {

    // Declaración de variables
    private EditText etBuscar;
    private ImageView ivCarrusel;
    private RecyclerView rvRecomendaciones;
    private LinearLayout llCategoriasChips;

    private LugarAdapter recomendacionesAdapter;
    private List<Lugar> listaRecomendaciones;

    /**
     * Constructor estático para crear instancia del fragment
     */
    public static InicioFragment newInstance() {
        return new InicioFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout del fragment
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);

        // Inicializar vistas
        inicializarVistas(view);

        // Configurar componentes
        configurarCarrusel();
        configurarCategoriasChips();
        configurarRecomendaciones();

        return view;
    }

    /**
     * Inicializa todas las vistas del fragment
     */
    private void inicializarVistas(View view) {
        etBuscar = view.findViewById(R.id.etBuscarInicio);
        ivCarrusel = view.findViewById(R.id.ivCarruselInicio);
        rvRecomendaciones = view.findViewById(R.id.rvRecomendaciones);
        llCategoriasChips = view.findViewById(R.id.llCategoriasChips);
    }

    /**
     * Configura el carrusel de imágenes (ViewPager o simple ImageView)
     */
    private void configurarCarrusel() {
        // Simulación de carrusel simple
        // En producción usar ViewPager2 con múltiples imágenes
        ivCarrusel.setImageResource(R.drawable.gradient_overlay);

        // Click en carrusel lleva a detalle
        ivCarrusel.setOnClickListener(v -> {
            // Abrir detalle del lugar destacado
            abrirDetalle(obtenerLugarDestacado());
        });
    }

    /**
     * Configura los chips de categorías (Naturaleza, Exterior, Interior, etc.)
     */
    private void configurarCategoriasChips() {
        // Crear chips dinámicamente
        String[] categorias = {"Naturaleza", "Exterior", "Interior", "Noche", "Tarde"};

        for (String categoria : categorias) {
            TextView chip = new TextView(getContext());
            chip.setText(categoria);
            chip.setPadding(24, 12, 24, 12);
            chip.setBackgroundResource(R.drawable.bg_chip);

            // Agregar margen
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 8, 8, 8);
            chip.setLayoutParams(params);

            // Agregar al contenedor
            llCategoriasChips.addView(chip);

            // Click en chip filtra por categoría
            chip.setOnClickListener(v -> filtrarPorCategoria(categoria));
        }
    }

    /**
     * Configura el RecyclerView de recomendaciones
     */
    private void configurarRecomendaciones() {
        // Inicializar lista de recomendaciones
        listaRecomendaciones = obtenerRecomendaciones();

        // Configurar adapter
        recomendacionesAdapter = new LugarAdapter(listaRecomendaciones, lugar -> {
            // Click en item abre detalle
            abrirDetalle(lugar);
        });

        // Configurar RecyclerView horizontal
        rvRecomendaciones.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        rvRecomendaciones.setAdapter(recomendacionesAdapter);
    }

    /**
     * Obtiene lista de lugares recomendados
     */
    private List<Lugar> obtenerRecomendaciones() {
        List<Lugar> lista = new ArrayList<>();

        // Datos de ejemplo
        lista.add(new Lugar(
                "Noche",
                "Experiencia nocturna única",
                145.00,
                "Aventura, Cultura",
                R.drawable.ic_launcher_background
        ));

        lista.add(new Lugar(
                "Tarde",
                "Disfruta de un atardecer espectacular",
                120.00,
                "Aventura, Naturaleza",
                R.drawable.ic_launcher_background
        ));

        lista.add(new Lugar(
                "Día",
                "Explora durante el día",
                100.00,
                "Cultura, Historia",
                R.drawable.ic_launcher_background
        ));

        return lista;
    }

    /**
     * Filtra lugares por categoría seleccionada
     */
    private void filtrarPorCategoria(String categoria) {
        // Implementar filtrado
        // recomendacionesAdapter.filtrar(categoria);
    }

    /**
     * Obtiene el lugar destacado del carrusel
     */
    private Lugar obtenerLugarDestacado() {
        return new Lugar(
                "Carnaval de Imágenes",
                "Un desfile lleno de color y tradición",
                150.00,
                "Aventura, Cultura",
                R.drawable.ic_launcher_background
        );
    }

    /**
     * Abre la pantalla de detalle del lugar
     */
    private void abrirDetalle(Lugar lugar) {
        Intent intent = new Intent(getActivity(), DetalleActivity.class);
        intent.putExtra("LUGAR", lugar);
        startActivity(intent);
    }
}