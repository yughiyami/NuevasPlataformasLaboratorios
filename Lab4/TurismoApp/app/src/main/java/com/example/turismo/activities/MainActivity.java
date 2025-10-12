package com.example.turismo.activities;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.turismo.R;

/**
 * Activity principal - Pantalla de Login
 * Permite al usuario autenticarse para acceder a la app
 */
public class MainActivity extends AppCompatActivity {

    // Declaración de variables
    private EditText etUsuario, etPassword;
    private Button btnLogin, btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar componentes
        inicializarVistas();

        // Configurar eventos de botones
        configurarEventos();
    }

    /**
     * Inicializa las vistas del layout
     */
    private void inicializarVistas() {
        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrar = findViewById(R.id.btnRegistrar);
    }

    /**
     * Configura los eventos de click de los botones
     */
    private void configurarEventos() {
        // Click en botón Login
        btnLogin.setOnClickListener(v -> realizarLogin());

        // Click en botón Registrar
        btnRegistrar.setOnClickListener(v -> irARegistro());
    }

    /**
     * Valida credenciales y accede al HomeActivity
     */
    private void realizarLogin() {
        String usuario = etUsuario.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validación básica
        if (usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación simple (en producción usar BD o API)
        if (usuario.equals("admin") && password.equals("123")) {
            // Login exitoso - ir a HomeActivity
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("USUARIO", usuario);
            startActivity(intent);
            finish(); // Cerrar MainActivity
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Navega a la pantalla de registro
     */
    private void irARegistro() {
        Toast.makeText(this, "Función de registro próximamente", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
        // startActivity(intent);
    }
}
