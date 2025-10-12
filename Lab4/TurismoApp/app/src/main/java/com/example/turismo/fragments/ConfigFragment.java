package com.example.turismo.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import com.example.turismo.R;

/**
 * ConfigFragment - Pantalla de configuración y notificaciones
 * Permite activar/desactivar notificaciones, cambiar idioma, ver historial
 */
public class ConfigFragment extends Fragment {

    // Declaración de variables
    private TextView tvTituloConfig;
    private SwitchCompat switchNotifDiarias, switchAlertasViaje;
    private LinearLayout llIdiomas, llHistorialActividades, llAcercaDe, llCreditos;

    private SharedPreferences preferences;

    /**
     * Constructor estático
     */
    public static ConfigFragment newInstance() {
        return new ConfigFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config, container, false);

        // Inicializar SharedPreferences
        preferences = getActivity().getSharedPreferences("ConfigApp", 0);

        // Inicializar vistas
        inicializarVistas(view);

        // Cargar configuración guardada
        cargarConfiguracion();

        // Configurar eventos
        configurarEventos();

        return view;
    }

    /**
     * Inicializa todas las vistas
     */
    private void inicializarVistas(View view) {
        tvTituloConfig = view.findViewById(R.id.tvTituloConfig);

        // Notificaciones
        switchNotifDiarias = view.findViewById(R.id.switchNotifDiarias);
        switchAlertasViaje = view.findViewById(R.id.switchAlertasViaje);

        // Opciones generales
        llIdiomas = view.findViewById(R.id.llIdiomas);
        llHistorialActividades = view.findViewById(R.id.llHistorialActividades);
        llAcercaDe = view.findViewById(R.id.llAcercaDe);
        llCreditos = view.findViewById(R.id.llCreditos);

        tvTituloConfig.setText("Configuración");
    }

    /**
     * Carga la configuración guardada previamente
     */
    private void cargarConfiguracion() {
        // Cargar estado de notificaciones
        boolean notifDiarias = preferences.getBoolean("notif_diarias", true);
        boolean alertasViaje = preferences.getBoolean("alertas_viaje", true);

        switchNotifDiarias.setChecked(notifDiarias);
        switchAlertasViaje.setChecked(alertasViaje);
    }

    /**
     * Configura los eventos de los componentes
     */
    private void configurarEventos() {
        // Switch Notificaciones Diarias
        switchNotifDiarias.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("notif_diarias", isChecked).apply();

            String mensaje = isChecked ?
                    "Notificaciones diarias activadas" :
                    "Notificaciones diarias desactivadas";
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        // Switch Alertas de Viaje
        switchAlertasViaje.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean("alertas_viaje", isChecked).apply();

            String mensaje = isChecked ?
                    "Alertas de viaje activadas" :
                    "Alertas de viaje desactivadas";
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        });

        // Click en Idiomas
        llIdiomas.setOnClickListener(v -> {
            mostrarDialogoIdiomas();
        });

        // Click en Historial de Actividades
        llHistorialActividades.setOnClickListener(v -> {
            abrirHistorialActividades();
        });

        // Click en Acerca De
        llAcercaDe.setOnClickListener(v -> {
            mostrarAcercaDe();
        });

        // Click en Créditos
        llCreditos.setOnClickListener(v -> {
            mostrarCreditos();
        });
    }

    /**
     * Muestra diálogo para seleccionar idioma
     */
    private void mostrarDialogoIdiomas() {
        String[] idiomas = {"Español", "English", "Português"};

        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Seleccionar Idioma");
        builder.setItems(idiomas, (dialog, which) -> {
            String idiomaSeleccionado = idiomas[which];
            preferences.edit().putString("idioma", idiomaSeleccionado).apply();

            Toast.makeText(getContext(),
                    "Idioma cambiado a: " + idiomaSeleccionado,
                    Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }

    /**
     * Abre la pantalla de historial de actividades
     */
    private void abrirHistorialActividades() {
        Toast.makeText(getContext(),
                "Historial de actividades - Próximamente",
                Toast.LENGTH_SHORT).show();

        // Implementar navegación a nueva pantalla
        // Intent intent = new Intent(getActivity(), HistorialActivity.class);
        // startActivity(intent);
    }

    /**
     * Muestra información Acerca De la aplicación
     */
    private void mostrarAcercaDe() {
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Acerca De");
        builder.setMessage(
                "App de Turismo Cultural\n\n" +
                        "Versión: 1.0.0\n\n" +
                        "Explora los lugares más emblemáticos de Arequipa " +
                        "y descubre la riqueza cultural de la región.\n\n" +
                        "Desarrollado con ❤️ para amantes del turismo."
        );
        builder.setPositiveButton("Cerrar", null);
        builder.show();
    }

    /**
     * Muestra los créditos de la aplicación
     */
    private void mostrarCreditos() {
        androidx.appcompat.app.AlertDialog.Builder builder =
                new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Créditos");
        builder.setMessage(
                "Desarrollado por:\n" +
                        "• Tu Nombre\n" +
                        "• Equipo de Desarrollo\n\n" +
                        "Diseño UI/UX:\n" +
                        "• Diseñador Principal\n\n" +
                        "Colaboradores:\n" +
                        "• Lista de colaboradores\n\n" +
                        "Agradecimientos especiales a todos los que " +
                        "hicieron posible este proyecto."
        );
        builder.setPositiveButton("Cerrar", null);
        builder.show();
    }

    /**
     * Notificaciones Pendientes - Sección adicional
     */
    private void mostrarNotificacionesPendientes() {
        // Implementar lógica para mostrar notificaciones no leídas
        // Podría ser en una nueva Activity o en un RecyclerView dentro del fragment
    }
}