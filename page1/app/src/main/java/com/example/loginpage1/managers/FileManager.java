package com.example.loginpage1.managers;

import android.content.Context;
import android.util.Log;

import com.example.loginpage1.models.Asistente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String TAG = "FileManager";
    private static final String FILE_NAME = "asistentes_conferencia.txt";
    private final Context context;

    public FileManager(Context context) {
        this.context = context;
    }

    /**
     * Guarda un asistente en el archivo de texto plano
     */
    public boolean guardarAsistente(Asistente asistente) {
        try {
            // Obtener el directorio de archivos internos de la aplicación
            File file = new File(context.getFilesDir(), FILE_NAME);

            // Crear FileWriter en modo append para no sobrescribir datos existentes
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Escribir los datos del asistente en formato separado por |
            bufferedWriter.write(asistente.toFileFormat());
            bufferedWriter.newLine();

            // Cerrar streams
            bufferedWriter.close();
            fileWriter.close();

            Log.d(TAG, "Asistente guardado exitosamente: " + asistente.getNombreCompleto());
            return true;

        } catch (IOException e) {
            Log.e(TAG, "Error al guardar asistente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lee todos los asistentes del archivo y los devuelve como lista
     */
    public List<Asistente> leerAsistentes() {
        List<Asistente> asistentes = new ArrayList<>();

        try {
            File file = new File(context.getFilesDir(), FILE_NAME);

            // Verificar si el archivo existe
            if (!file.exists()) {
                Log.w(TAG, "El archivo no existe aún. No hay asistentes registrados.");
                return asistentes; // Devolver lista vacía
            }

            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String linea;
            int numeroLinea = 1;

            // Leer línea por línea
            while ((linea = bufferedReader.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    try {
                        Asistente asistente = Asistente.fromFileFormat(linea);
                        if (asistente != null) {
                            asistentes.add(asistente);
                            Log.d(TAG, "Asistente leído: " + asistente.getNombreCompleto());
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

            Log.i(TAG, "Total de asistentes leídos: " + asistentes.size());

        } catch (IOException e) {
            Log.e(TAG, "Error al leer archivo: " + e.getMessage());
        }

        return asistentes;
    }

    /**
     * Muestra todos los asistentes en el Log (consola)
     */
    public void mostrarAsistentesEnConsola() {
        List<Asistente> asistentes = leerAsistentes();

        if (asistentes.isEmpty()) {
            Log.i(TAG, "═══════════════════════════════════");
            Log.i(TAG, "   NO HAY ASISTENTES REGISTRADOS   ");
            Log.i(TAG, "═══════════════════════════════════");
            return;
        }

        Log.i(TAG, "═══════════════════════════════════════════════════════");
        Log.i(TAG, "           LISTA DE ASISTENTES REGISTRADOS             ");
        Log.i(TAG, "═══════════════════════════════════════════════════════");
        Log.i(TAG, "Total de asistentes: " + asistentes.size());
        Log.i(TAG, "═══════════════════════════════════════════════════════");

        int contador = 1;
        for (Asistente asistente : asistentes) {
            Log.i(TAG, "ASISTENTE #" + contador);
            Log.i(TAG, asistente.toString());
            contador++;
        }

        Log.i(TAG, "═══════════════════════════════════════════════════════");
        Log.i(TAG, "           FIN DE LA LISTA DE ASISTENTES               ");
        Log.i(TAG, "═══════════════════════════════════════════════════════");
    }

    /**
     * Obtiene estadísticas básicas de los asistentes registrados
     */
    public void mostrarEstadisticas() {
        List<Asistente> asistentes = leerAsistentes();

        if (asistentes.isEmpty()) {
            Log.i(TAG, "No hay datos para mostrar estadísticas.");
            return;
        }

        Log.i(TAG, "═══════════════ ESTADÍSTICAS ═══════════════");
        Log.i(TAG, "Total de asistentes registrados: " + asistentes.size());

        // Contar grupos sanguíneos
        Log.i(TAG, "Distribución por grupo sanguíneo:");
        // Aquí podrías implementar lógica adicional para contar grupos sanguíneos

        Log.i(TAG, "═══════════════════════════════════════════");
    }

    /**
     * Verifica si existe el archivo de asistentes
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
     * Elimina el archivo de asistentes (para testing o reset)
     */
    public boolean eliminarArchivo() {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            if (file.exists()) {
                boolean eliminado = file.delete();
                Log.i(TAG, eliminado ? "Archivo eliminado exitosamente" : "No se pudo eliminar el archivo");
                return eliminado;
            } else {
                Log.i(TAG, "El archivo no existe");
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al eliminar archivo: " + e.getMessage());
            return false;
        }
    }
}