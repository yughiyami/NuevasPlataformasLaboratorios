package com.example.loginpage1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginpage1.R;
import com.example.loginpage1.managers.UserFileManager;
import com.example.loginpage1.models.User;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    public static final String EXTRA_USER_DATA = "user_data";

    // Componentes de la interfaz
    private TextInputEditText etUsername, etPassword;
    private Button btnLogin, btnCrearCuenta;

    // Manejador de archivos de usuarios
    private UserFileManager userFileManager;

    // ActivityResultLauncher para manejar el resultado de AccountActivity
    private ActivityResultLauncher<Intent> createAccountLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Register the OnBackPressedCallback
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Manejar el botón back - confirmar salida de la aplicación
                finishAffinity(); // Cerrar toda la aplicación
            }
        });
        // Inicializar UserFileManager
        userFileManager = new UserFileManager(this);

        // Inicializar componentes de la interfaz
        initializeViews();

        // Configurar ActivityResultLauncher
        setupActivityResultLauncher();

        // Configurar listeners de botones
        setupButtonListeners();

        Log.i(TAG, "LoginActivity iniciada correctamente");
        Log.i(TAG, "Ruta del archivo de usuarios: " + userFileManager.getRutaArchivo());
    }

    /**
     * Inicializar todas las vistas
     */
    private void initializeViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
    }

    /**
     * Configurar el ActivityResultLauncher para crear cuenta
     */
    private void setupActivityResultLauncher() {
        createAccountLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null && data.hasExtra(EXTRA_USER_DATA)) {
                                String userData = data.getStringExtra(EXTRA_USER_DATA);
                                procesarNuevaCuenta(userData);
                            }
                        } else {
                            Log.d(TAG, "Creación de cuenta cancelada o fallida");
                        }
                    }
                }
        );
    }

    /**
     * Configurar listeners para los botones
     */
    private void setupButtonListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarLogin();
            }
        });

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCrearCuenta();
            }
        });
    }

    /**
     * Método para realizar el login
     */
    private void realizarLogin() {
        try {
            // Obtener datos del formulario
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validar datos
            if (!validarCredenciales(username, password)) {
                return;
            }

            // Consultar en el archivo de cuentas.txt
            User usuarioAutenticado = userFileManager.autenticarUsuario(username, password);

            if (usuarioAutenticado != null) {
                // Usuario encontrado - Login exitoso
                Log.i(TAG, "Login exitoso para usuario: " + username);

                // Mostrar mensaje de bienvenida
                Toast.makeText(this, "Bienvenido " + usuarioAutenticado.getFullName(),
                        Toast.LENGTH_LONG).show();

                // Inicializar HomeActivity
                inicializarHomeActivity(usuarioAutenticado);

            } else {
                // Usuario no encontrado
                Log.w(TAG, "Intento de login fallido para usuario: " + username);
                Toast.makeText(this, "Cuenta no encontrada", Toast.LENGTH_SHORT).show();

                // Limpiar campos
                limpiarCampos();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error durante el login: " + e.getMessage());
            Toast.makeText(this, "Error inesperado durante el login", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Validar las credenciales ingresadas
     */
    private boolean validarCredenciales(String username, String password) {
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

        return true;
    }

    /**
     * Inicializar HomeActivity con los datos del usuario
     */
    private void inicializarHomeActivity(User usuario) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("username", usuario.getUsername());
        intent.putExtra("fullName", usuario.getFullName());
        intent.putExtra("email", usuario.getEmail());

        // Limpiar el activity stack para que no se pueda volver al login con el botón atrás
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish(); // Cerrar LoginActivity
    }

    /**
     * Abrir AccountActivity para crear nueva cuenta
     */
    private void abrirCrearCuenta() {
        Intent intent = new Intent(this, AccountActivity.class);
        createAccountLauncher.launch(intent);
        Log.d(TAG, "Abriendo AccountActivity para crear nueva cuenta");
    }

    /**
     * Procesar los datos de la nueva cuenta creada
     */
    private void procesarNuevaCuenta(String userData) {
        try {
            Log.d(TAG, "Procesando nueva cuenta: " + userData);

            // Convertir JSON string a objeto User
            User nuevoUsuario = User.fromJSON(userData);

            if (nuevoUsuario != null && nuevoUsuario.isValid()) {
                // Guardar en archivo
                boolean guardado = userFileManager.guardarUsuario(nuevoUsuario);

                if (guardado) {
                    Log.i(TAG, "Nueva cuenta creada exitosamente: " + nuevoUsuario.getUsername());
                    Toast.makeText(this, "¡Cuenta creada exitosamente! Ahora puedes iniciar sesión",
                            Toast.LENGTH_LONG).show();

                    // Prellenar el username en el formulario de login
                    etUsername.setText(nuevoUsuario.getUsername());
                    etPassword.requestFocus();

                } else {
                    Log.e(TAG, "Error al guardar nueva cuenta");
                    Toast.makeText(this, "Error al crear la cuenta. Es posible que el usuario ya exista",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Log.e(TAG, "Datos de usuario inválidos recibidos");
                Toast.makeText(this, "Error: datos de usuario inválidos", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e(TAG, "Error al procesar nueva cuenta: " + e.getMessage());
            Toast.makeText(this, "Error al procesar la nueva cuenta", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Limpiar los campos de entrada
     */
    private void limpiarCampos() {
        etPassword.setText("");
        etUsername.setError(null);
        etPassword.setError(null);
        etUsername.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "LoginActivity reanudada");
        // Mostrar estadísticas de usuarios en consola (para debugging)
        userFileManager.mostrarUsuariosEnConsola();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "LoginActivity pausada");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "LoginActivity destruida");
    }


}