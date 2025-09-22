package com.example.loginpage1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.loginpage1.R;
import com.example.loginpage1.models.User;
import com.google.android.material.textfield.TextInputEditText;

public class AccountActivity extends AppCompatActivity {

    private static final String TAG = "AccountActivity";

    // Componentes de la interfaz
    private TextInputEditText etFullName, etEmail, etUsername, etPassword, etConfirmPassword;
    private Button btnAceptar, btnCancelar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Inicializar componentes de la interfaz
        initializeViews();

        // Configurar toolbar
        setupToolbar();

        // Configurar listeners de botones
        setupButtonListeners();

        Log.i(TAG, "AccountActivity iniciada correctamente");
    }

    /**
     * Inicializar todas las vistas
     */
    private void initializeViews() {
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnAceptar = findViewById(R.id.btnAceptar);
        btnCancelar = findViewById(R.id.btnCancelar);
        toolbar = findViewById(R.id.toolbar);
    }

    /**
     * Configurar el toolbar
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarCreacion();
            }
        });
    }

    /**
     * Configurar listeners para los botones
     */
    private void setupButtonListeners() {
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearCuenta();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarCreacion();
            }
        });
    }

    /**
     * Método para crear una nueva cuenta
     */
    private void crearCuenta() {
        try {
            // Obtener datos del formulario
            String fullName = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Validar datos
            if (!validarDatos(fullName, email, username, password, confirmPassword)) {
                return;
            }

            // Crear objeto User
            User nuevoUsuario = new User(username, password, email, fullName);

            // Serializar como JSON
            String userData = nuevoUsuario.toJSON().toString();

            // Enviar datos a LoginActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra(LoginActivity.EXTRA_USER_DATA, userData);
            setResult(RESULT_OK, resultIntent);

            // Log de confirmación
            Log.i(TAG, "Cuenta creada exitosamente: " + username);
            Log.d(TAG, "Datos del usuario: " + userData);

            // Mostrar mensaje de éxito y cerrar actividad
            Toast.makeText(this, "¡Cuenta creada exitosamente!", Toast.LENGTH_SHORT).show();
            finish();

        } catch (Exception e) {
            Log.e(TAG, "Error al crear cuenta: " + e.getMessage());
            Toast.makeText(this, "Error inesperado al crear cuenta", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Validar todos los datos del formulario
     */
    private boolean validarDatos(String fullName, String email, String username, String password, String confirmPassword) {

        // Validar nombre completo
        if (fullName.isEmpty()) {
            etFullName.setError("El nombre completo es obligatorio");
            etFullName.requestFocus();
            return false;
        }

        if (fullName.length() < 2) {
            etFullName.setError("El nombre debe tener al menos 2 caracteres");
            etFullName.requestFocus();
            return false;
        }

        // Validar email
        if (email.isEmpty()) {
            etEmail.setError("El email es obligatorio");
            etEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Ingrese un email válido");
            etEmail.requestFocus();
            return false;
        }

        // Validar username
        if (username.isEmpty()) {
            etUsername.setError("El username es obligatorio");
            etUsername.requestFocus();
            return false;
        }

        if (username.length() < 3) {
            etUsername.setError("El username debe tener al menos 3 caracteres");
            etUsername.requestFocus();
            return false;
        }

        if (username.length() > 20) {
            etUsername.setError("El username no puede tener más de 20 caracteres");
            etUsername.requestFocus();
            return false;
        }

        // Validar que el username solo contenga caracteres alfanuméricos
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            etUsername.setError("El username solo puede contener letras, números y guiones bajos");
            etUsername.requestFocus();
            return false;
        }

        // Validar password
        if (password.isEmpty()) {
            etPassword.setError("La contraseña es obligatoria");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 4) {
            etPassword.setError("La contraseña debe tener al menos 4 caracteres");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() > 50) {
            etPassword.setError("La contraseña no puede tener más de 50 caracteres");
            etPassword.requestFocus();
            return false;
        }

        // Validar confirmación de password
        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Debe confirmar la contraseña");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Las contraseñas no coinciden");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Cancelar la creación de cuenta y regresar a LoginActivity
     */
    private void cancelarCreacion() {
        Log.d(TAG, "Creación de cuenta cancelada");
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Limpiar todos los campos del formulario
     */
    private void limpiarFormulario() {
        etFullName.setText("");
        etEmail.setText("");
        etUsername.setText("");
        etPassword.setText("");
        etConfirmPassword.setText("");

        // Quitar errores
        etFullName.setError(null);
        etEmail.setError(null);
        etUsername.setError(null);
        etPassword.setError(null);
        etConfirmPassword.setError(null);

        // Focus en el primer campo
        etFullName.requestFocus();

        Log.d(TAG, "Formulario limpiado");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "AccountActivity reanudada");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "AccountActivity pausada");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "AccountActivity destruida");
    }
}