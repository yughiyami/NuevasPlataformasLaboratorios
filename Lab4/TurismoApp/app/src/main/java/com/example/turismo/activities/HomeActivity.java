package com.example.turismo.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.turismo.R;
import com.example.turismo.fragments.*;

/**
 * HomeActivity - Contenedor principal de la aplicación
 * Gestiona la navegación entre fragments mediante BottomNavigationView
 */
public class HomeActivity extends AppCompatActivity {

    // Declaración de variables
    private BottomNavigationView bottomNavigation;
    private FragmentManager fragmentManager;

    // Fragments de la aplicación
    private InicioFragment inicioFragment;
    private ListaLugaresFragment listaLugaresFragment;
    private MapaFragment mapaFragment;
    private FavoritosFragment favoritosFragment;
    private ConfigFragment configFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inicializar componentes
        inicializarVistas();

        // Configurar FragmentManager
        fragmentManager = getSupportFragmentManager();

        // Cargar fragment inicial (Inicio)
        if (savedInstanceState == null) {
            cargarFragment(getInicioFragment());
        }

        // Configurar navegación
        configurarBottomNavigation();
    }

    /**
     * Inicializa las vistas del layout
     */
    private void inicializarVistas() {
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Seleccionar item inicial
        bottomNavigation.setSelectedItemId(R.id.menu_inicio);
    }

    /**
     * Configura el BottomNavigationView para cambiar entre fragments
     */
    private void configurarBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            // Determinar qué fragment cargar según el item seleccionado
            if (itemId == R.id.menu_inicio) {
                cargarFragment(getInicioFragment());
                return true;
            }
            else if (itemId == R.id.menu_busqueda) {
                cargarFragment(getListaLugaresFragment());
                return true;
            }
            else if (itemId == R.id.menu_mapa) {
                cargarFragment(getMapaFragment());
                return true;
            }
            else if (itemId == R.id.menu_favoritos) {
                cargarFragment(getFavoritosFragment());
                return true;
            }
            else if (itemId == R.id.menu_config) {
                cargarFragment(getConfigFragment());
                return true;
            }

            return false;
        });
    }

    /**
     * Carga un fragment en el contenedor
     * @param fragment Fragment a cargar
     */
    private void cargarFragment(Fragment fragment) {
        if (fragmentManager != null && fragment != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer, fragment);
            transaction.commit();
        }
    }

    // Métodos Lazy Loading para crear fragments solo cuando se necesiten

    private Fragment getInicioFragment() {
        if (inicioFragment == null) {
            inicioFragment = InicioFragment.newInstance();
        }
        return inicioFragment;
    }

    private Fragment getListaLugaresFragment() {
        if (listaLugaresFragment == null) {
            listaLugaresFragment = ListaLugaresFragment.newInstance();
        }
        return listaLugaresFragment;
    }

    private Fragment getMapaFragment() {
        if (mapaFragment == null) {
            mapaFragment = MapaFragment.newInstance();
        }
        return mapaFragment;
    }

    private Fragment getFavoritosFragment() {
        if (favoritosFragment == null) {
            favoritosFragment = FavoritosFragment.newInstance();
        }
        return favoritosFragment;
    }

    private Fragment getConfigFragment() {
        if (configFragment == null) {
            configFragment = ConfigFragment.newInstance();
        }
        return configFragment;
    }
}