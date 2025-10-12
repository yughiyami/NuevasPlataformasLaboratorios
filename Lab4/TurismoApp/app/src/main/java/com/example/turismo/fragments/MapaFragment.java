package com.example.turismo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.turismo.R;

/**
 * MapaFragment - Muestra mapa con ubicaciones de lugares turísticos
 * Usa Google Maps API para mostrar marcadores
 */
public class MapaFragment extends Fragment {

    // Variables para Google Maps
    // private GoogleMap googleMap;
    // private MapView mapView;

    /**
     * Constructor estático
     */
    public static MapaFragment newInstance() {
        return new MapaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);

        // Inicializar mapa
        inicializarMapa(view);

        return view;
    }

    /**
     * Inicializa el mapa de Google Maps
     * NOTA: Requiere Google Maps API configurada en el proyecto
     */
    private void inicializarMapa(View view) {
        // IMPLEMENTACIÓN BÁSICA SIN GOOGLE MAPS
        // Para implementar Google Maps completo:

        /*
        // 1. Agregar dependencia en build.gradle:
        // implementation 'com.google.android.gms:play-services-maps:18.1.0'

        // 2. Obtener API Key de Google Cloud Console

        // 3. Agregar en AndroidManifest.xml:
        // <meta-data
        //     android:name="com.google.android.geo.API_KEY"
        //     android:value="TU_API_KEY"/>

        // 4. Código de implementación:

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                configurarMapa();
            }
        });
        */

        // Mensaje informativo mientras se configura
        Toast.makeText(getContext(),
                "Mapa de ubicaciones - Requiere configurar Google Maps API",
                Toast.LENGTH_LONG).show();
    }

    /**
     * Configura el mapa con marcadores y estilos
     */
    private void configurarMapa() {
        /*
        // Ubicación de Arequipa
        LatLng arequipa = new LatLng(-16.4090, -71.5375);

        // Mover cámara a Arequipa
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arequipa, 12));

        // Agregar marcadores de lugares turísticos
        agregarMarcadores();

        // Configurar estilo del mapa
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Habilitar controles
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        */
    }

    /**
     * Agrega marcadores de lugares turísticos en el mapa
     */
    private void agregarMarcadores() {
        /*
        // Cañón del Colca
        LatLng colca = new LatLng(-15.6050, -71.8872);
        googleMap.addMarker(new MarkerOptions()
            .position(colca)
            .title("Cañón del Colca")
            .snippet("S/ 145.00"));

        // Monasterio de Santa Catalina
        LatLng monasterio = new LatLng(-16.3968, -71.5369);
        googleMap.addMarker(new MarkerOptions()
            .position(monasterio)
            .title("Monasterio de Santa Catalina")
            .snippet("S/ 35.00"));

        // Volcán Misti
        LatLng misti = new LatLng(-16.2938, -71.4092);
        googleMap.addMarker(new MarkerOptions()
            .position(misti)
            .title("Volcán Misti")
            .snippet("S/ 145.00"));

        // Plaza de Armas
        LatLng plaza = new LatLng(-16.3988, -71.5369);
        googleMap.addMarker(new MarkerOptions()
            .position(plaza)
            .title("Plaza de Armas")
            .snippet("Gratis"));

        // Click en marcador abre detalle
        googleMap.setOnMarkerClickListener(marker -> {
            // Obtener lugar por título y abrir detalle
            return false;
        });
        */
    }

    @Override
    public void onResume() {
        super.onResume();
        // if (mapView != null) mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // if (mapView != null) mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // if (mapView != null) mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // if (mapView != null) mapView.onLowMemory();
    }
}