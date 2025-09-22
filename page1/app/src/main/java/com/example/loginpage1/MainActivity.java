package com.example.loginpage1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginpage1.activities.LoginActivity;

/**
 * MainActivity - Actividad de inicio que redirige al sistema de login
 * Esta clase actúa como punto de entrada y redirige al LoginActivity
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Aplicación iniciada - Redirigiendo al sistema de login");

        // Inicializar el sistema de login
        iniciarSistemaLogin();
    }

    /**
     * Método para inicializar el sistema de login
     */
    private void iniciarSistemaLogin() {
        try {
            // Crear intent para LoginActivity
            Intent loginIntent = new Intent(this, LoginActivity.class);

            // Limpiar el stack de activities para que no se pueda regresar a MainActivity
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // Iniciar LoginActivity
            startActivity(loginIntent);

            // Cerrar MainActivity
            finish();

            Log.i(TAG, "Sistema de login iniciado correctamente");

        } catch (Exception e) {
            Log.e(TAG, "Error al iniciar sistema de login: " + e.getMessage());
            // En caso de error, mantener MainActivity como respaldo
            cargarInterfazRespaldo();
        }
    }

    /**
     * Método de respaldo en caso de que falle el sistema de login
     * Carga la interfaz original de registro de asistentes
     */
    private void cargarInterfazRespaldo() {
        Log.w(TAG, "Cargando interfaz de respaldo - Sistema de registro directo");

        // Crear intent para HomeActivity (que contiene el formulario de asistentes)
        Intent homeIntent = new Intent(this, com.example.loginpage1.activities.HomeActivity.class);

        // Pasar datos de usuario por defecto para el respaldo
        homeIntent.putExtra("username", "admin");
        homeIntent.putExtra("fullName", "Administrador");
        homeIntent.putExtra("email", "admin@sistema.com");

        startActivity(homeIntent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity reanudada");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity destruida");
    }
}