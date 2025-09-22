package com.example.loginpage1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.loginpage1.managers.FileManager;
import com.example.loginpage1.R;
import com.example.loginpage1.models.Asistente;
import com.google.android.material.textfield.TextInputEditText;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    // Componentes de la interfaz
    private TextView tvWelcome;
    private TextInputEditText etNombres, etApellidos, etCorreo, etTelefono, etOrganizacion, etAreaInteres;
    private Spinner spGrupoSanguineo;
    private Button btnRegistrar, btnMostrarDatos;
    private Toolbar toolbar;

    // Manejador de archivos
    private FileManager fileManager;

    // Datos del usuario logueado
    private String currentUsername;
    private String currentUserFullName;
    private String currentUserEmail;

    // Grupos sanguíneos disponibles
    private String[] gruposSanguineos = {
            "Seleccionar grupo sanguíneo",
            "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Obtener datos del usuario logueado
        obtenerDatosUsuario();

        // Inicializar FileManager
        fileManager = new FileManager(this);

        // Inicializar componentes de la interfaz
        initializeViews();

        // Configurar toolbar
        setupToolbar();

        // Configurar mensaje de bienvenida
        setupWelcomeMessage();

        // Configurar spinner
        setupSpinner();

        // Configurar listeners de botones
        setupButtonListeners();
        setupBackPressedCallback();

        Log.i(TAG, "HomeActivity iniciada correctamente para usuario: " + currentUsername);
        Log.i(TAG, "Ruta del archivo: " + fileManager.getRutaArchivo());
    }

    /**
     * Obtener datos del usuario desde el Intent
     */

    private void setupBackPressedCallback() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Manejar el botón back - confirmar logout
                realizarLogout();
            }
        });
    }
    private void obtenerDatosUsuario() {
        Intent intent = getIntent();
        currentUsername = intent.getStringExtra("username");
        currentUserFullName = intent.getStringExtra("fullName");
        currentUserEmail = intent.getStringExtra("email");

        if (currentUsername == null || currentUserFullName == null) {
            Log.e(TAG, "Datos de usuario faltantes. Redirigiendo a LoginActivity");
            redirigirALogin();
        }
    }

    /**
     * Inicializar todas las vistas
     */
    private void initializeViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        etNombres = findViewById(R.id.etNombres);
        etApellidos = findViewById(R.id.etApellidos);
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);
        etOrganizacion = findViewById(R.id.etOrganizacion);
        etAreaInteres = findViewById(R.id.etAreaInteres);
        spGrupoSanguineo = findViewById(R.id.spGrupoSanguineo);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnMostrarDatos = findViewById(R.id.btnMostrarDatos);
        toolbar = findViewById(R.id.toolbar);
    }

    /**
     * Configurar el toolbar
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Sistema de Registro");
            getSupportActionBar().setSubtitle("Gestión de Asistentes");
        }
    }

    /**
     * Configurar mensaje de bienvenida
     */
    private void setupWelcomeMessage() {
        String welcomeMessage = "Bienvenido, " + currentUserFullName;
        tvWelcome.setText(welcomeMessage);
        tvWelcome.setVisibility(View.VISIBLE);
    }

    /**
     * Configurar el spinner de grupos sanguíneos
     */
    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                gruposSanguineos
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGrupoSanguineo.setAdapter(adapter);
    }

    /**
     * Configurar listeners para los botones
     */
    private void setupButtonListeners() {
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarAsistente();
            }
        });

        btnMostrarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatos();
            }
        });
    }

    /**
     * Método para registrar un nuevo asistente
     */
    private void registrarAsistente() {
        try {
            // Obtener datos del formulario
            String nombres = etNombres.getText().toString().trim();
            String apellidos = etApellidos.getText().toString().trim();
            String correo = etCorreo.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();
            String organizacion = etOrganizacion.getText().toString().trim();
            String areaInteres = etAreaInteres.getText().toString().trim();

            int posicionGrupoSanguineo = spGrupoSanguineo.getSelectedItemPosition();
            String grupoSanguineo = posicionGrupoSanguineo > 0 ?
                    gruposSanguineos[posicionGrupoSanguineo] : "";

            // Validar datos obligatorios
            if (!validarDatos(nombres, apellidos, correo, telefono, grupoSanguineo)) {
                return;
            }

            // Crear objeto Asistente
            Asistente nuevoAsistente = new Asistente(
                    nombres, apellidos, correo, telefono,
                    grupoSanguineo, organizacion, areaInteres
            );

            // Guardar en archivo
            boolean guardado = fileManager.guardarAsistente(nuevoAsistente);

            if (guardado) {
                // Mostrar mensaje de éxito
                Toast.makeText(this, "¡Asistente registrado exitosamente!", Toast.LENGTH_LONG).show();

                // Log de confirmación
                Log.i(TAG, "Asistente registrado por " + currentUsername + ": " + nuevoAsistente.getNombreCompleto());
                Log.d(TAG, nuevoAsistente.toString());

                // Limpiar formulario
                limpiarFormulario();

            } else {
                Toast.makeText(this, "Error al registrar asistente. Intente de nuevo.", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error al guardar asistente");
            }

        } catch (Exception e) {
            Log.e(TAG, "Error al registrar asistente: " + e.getMessage());
            Toast.makeText(this, "Error inesperado al registrar asistente", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Validar que los datos obligatorios estén completos
     */
    private boolean validarDatos(String nombres, String apellidos, String correo, String telefono, String grupoSanguineo) {
        // Validar nombres
        if (nombres.isEmpty()) {
            etNombres.setError("Los nombres son obligatorios");
            etNombres.requestFocus();
            return false;
        }

        // Validar apellidos
        if (apellidos.isEmpty()) {
            etApellidos.setError("Los apellidos son obligatorios");
            etApellidos.requestFocus();
            return false;
        }

        // Validar correo
        if (correo.isEmpty()) {
            etCorreo.setError("El correo es obligatorio");
            etCorreo.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreo.setError("Ingrese un correo válido");
            etCorreo.requestFocus();
            return false;
        }

        // Validar teléfono
        if (telefono.isEmpty()) {
            etTelefono.setError("El teléfono es obligatorio");
            etTelefono.requestFocus();
            return false;
        }

        if (telefono.length() < 7) {
            etTelefono.setError("Ingrese un teléfono válido");
            etTelefono.requestFocus();
            return false;
        }

        // Validar grupo sanguíneo
        if (grupoSanguineo.isEmpty()) {
            Toast.makeText(this, "Seleccione un grupo sanguíneo", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * Mostrar todos los datos almacenados usando la clase Log
     */
    private void mostrarDatos() {
        try {
            Log.i(TAG, "╔══════════════════════════════════════════╗");
            Log.i(TAG, "    MOSTRANDO DATOS DE ASISTENTES          ");
            Log.i(TAG, "    Usuario: " + currentUsername + "                   ");
            Log.i(TAG, "╚══════════════════════════════════════════╝");

            // Verificar si existe el archivo
            if (!fileManager.existeArchivo()) {
                Log.w(TAG, "No se encontró archivo de asistentes");
                Toast.makeText(this, "No hay asistentes registrados aún", Toast.LENGTH_SHORT).show();
                return;
            }

            // Mostrar asistentes en consola usando FileManager
            fileManager.mostrarAsistentesEnConsola();

            // Mostrar estadísticas
            fileManager.mostrarEstadisticas();

            // Mensaje para el usuario
            Toast.makeText(this, "Datos mostrados en la consola (Log). Revise el LogCat.", Toast.LENGTH_LONG).show();

            Log.i(TAG, "Fin de la visualización de datos");

        } catch (Exception e) {
            Log.e(TAG, "Error al mostrar datos: " + e.getMessage());
            Toast.makeText(this, "Error al leer los datos", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Limpiar todos los campos del formulario
     */
    private void limpiarFormulario() {
        etNombres.setText("");
        etApellidos.setText("");
        etCorreo.setText("");
        etTelefono.setText("");
        etOrganizacion.setText("");
        etAreaInteres.setText("");
        spGrupoSanguineo.setSelection(0);

        // Quitar errores
        etNombres.setError(null);
        etApellidos.setError(null);
        etCorreo.setError(null);
        etTelefono.setError(null);

        // Focus en el primer campo
        etNombres.requestFocus();

        Log.d(TAG, "Formulario limpiado");
    }

    /**
     * Redirigir a LoginActivity
     */
    private void redirigirALogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Realizar logout
     */
    private void realizarLogout() {
        Log.i(TAG, "Usuario " + currentUsername + " realizó logout");
        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
        redirigirALogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            realizarLogout();
            return true;
        } else if (id == R.id.action_user_info) {
            mostrarInfoUsuario();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Mostrar información del usuario logueado
     */
    private void mostrarInfoUsuario() {
        String info = "Usuario: " + currentUsername + "\n" +
                "Nombre: " + currentUserFullName + "\n" +
                "Email: " + currentUserEmail;

        Toast.makeText(this, info, Toast.LENGTH_LONG).show();
        Log.i(TAG, "Información del usuario actual: " + info);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "HomeActivity reanudada para usuario: " + currentUsername);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "HomeActivity pausada");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "HomeActivity destruida");
    }


}