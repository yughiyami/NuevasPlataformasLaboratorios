package com.example.loginpage1.managers;

import android.content.Context;
import android.util.Log;
import com.example.loginpage1.models.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserFileManager {
    private static final String TAG = "UserFileManager";
    private static final String FILE_NAME = "cuentas.txt";
    private final Context context;

    public UserFileManager(Context context) {
        this.context = context;
    }

    /**
     * Guarda un usuario en el archivo de texto plano
     */
    public boolean guardarUsuario(User user) {
        try {
            // Verificar si el usuario ya existe
            if (existeUsuario(user.getUsername())) {
                Log.w(TAG, "El usuario ya existe: " + user.getUsername());
                return false;
            }

            // Obtener el directorio de archivos internos de la aplicación
            File file = new File(context.getFilesDir(), FILE_NAME);

            // Crear FileWriter en modo append para no sobrescribir datos existentes
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Escribir los datos del usuario en formato JSON
            bufferedWriter.write(user.toJSON().toString());
            bufferedWriter.newLine();

            // Cerrar streams
            bufferedWriter.close();
            fileWriter.close();

            Log.d(TAG, "Usuario guardado exitosamente: " + user.getUsername());
            return true;

        } catch (IOException e) {
            Log.e(TAG, "Error al guardar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lee todos los usuarios del archivo y los devuelve como lista
     */
    public List<User> leerUsuarios() {
        List<User> usuarios = new ArrayList<>();

        try {
            File file = new File(context.getFilesDir(), FILE_NAME);

            // Verificar si el archivo existe
            if (!file.exists()) {
                Log.w(TAG, "El archivo de usuarios no existe aún. No hay usuarios registrados.");
                return usuarios; // Devolver lista vacía
            }

            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String linea;
            int numeroLinea = 1;

            // Leer línea por línea
            while ((linea = bufferedReader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    try {
                        User user = User.fromJSON(linea);
                        if (user != null) {
                            usuarios.add(user);
                            Log.d(TAG, "Usuario leído: " + user.getUsername());
                        } else {
                            Log.w(TAG, "Error al parsear línea " + numeroLinea + ": " + linea);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error al procesar línea " + numeroLinea + ": " + e.getMessage());
                    }
                }
                numeroLinea++;
            }

            // Cerrar streams
            bufferedReader.close();
            fileReader.close();

            Log.i(TAG, "Total de usuarios leídos: " + usuarios.size());

        } catch (IOException e) {
            Log.e(TAG, "Error al leer archivo de usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    /**
     * Verifica si un usuario existe en el archivo
     */
    public boolean existeUsuario(String username) {
        List<User> usuarios = leerUsuarios();
        for (User user : usuarios) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Autentica un usuario con username y password
     */
    public User autenticarUsuario(String username, String password) {
        List<User> usuarios = leerUsuarios();
        for (User user : usuarios) {
            if (user.checkCredentials(username, password)) {
                Log.i(TAG, "Usuario autenticado correctamente: " + username);
                return user;
            }
        }
        Log.w(TAG, "Credenciales incorrectas para: " + username);
        return null;
    }

    /**
     * Muestra todos los usuarios en el Log (consola)
     */
    public void mostrarUsuariosEnConsola() {
        List<User> usuarios = leerUsuarios();

        if (usuarios.isEmpty()) {
            Log.i(TAG, "╔═══════════════════════════════════╗");
            Log.i(TAG, "   NO HAY USUARIOS REGISTRADOS     ");
            Log.i(TAG, "╚═══════════════════════════════════╝");
            return;
        }

        Log.i(TAG, "╔═══════════════════════════════════════════════════════╗");
        Log.i(TAG, "           LISTA DE USUARIOS REGISTRADOS              ");
        Log.i(TAG, "╠═══════════════════════════════════════════════════════╣");
        Log.i(TAG, "Total de usuarios: " + usuarios.size());
        Log.i(TAG, "╠═══════════════════════════════════════════════════════╣");

        int contador = 1;
        for (User user : usuarios) {
            Log.i(TAG, "USUARIO #" + contador);
            Log.i(TAG, user.toString());
            contador++;
        }

        Log.i(TAG, "╚═══════════════════════════════════════════════════════╝");
        Log.i(TAG, "           FIN DE LA LISTA DE USUARIOS                 ");
        Log.i(TAG, "╚═══════════════════════════════════════════════════════╝");
    }

    /**
     * Verifica si existe el archivo de usuarios
     */
    public boolean existeArchivo() {
        File file = new File(context.getFilesDir(), FILE_NAME);
        return file.exists();
    }

    /**
     * Obtiene la ruta completa del archivo
     */
    public String getRutaArchivo() {
        File file = new File(context.getFilesDir(), FILE_NAME);
        return file.getAbsolutePath();
    }

    /**
     * Elimina el archivo de usuarios (para testing o reset)
     */
    public boolean eliminarArchivo() {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (file.exists()) {
                boolean eliminado = file.delete();
                Log.i(TAG, eliminado ? "Archivo de usuarios eliminado exitosamente" : "No se pudo eliminar el archivo");
                return eliminado;
            } else {
                Log.i(TAG, "El archivo de usuarios no existe");
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al eliminar archivo de usuarios: " + e.getMessage());
            return false;
        }
    }
}