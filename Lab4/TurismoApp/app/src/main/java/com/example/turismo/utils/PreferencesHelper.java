package com.example.turismo.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * PreferencesHelper - Manejo centralizado de SharedPreferences
 * Guarda configuraciones, sesión de usuario, y preferencias de la app
 */
public class PreferencesHelper {

    private static final String PREF_NAME = "TurismoPrefs";
    private static final String KEY_FIRST_TIME = "first_time";
    private static final String KEY_USER_LOGGED = "user_logged";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NOTIF_DIARIAS = "notif_diarias";
    private static final String KEY_ALERTAS_VIAJE = "alertas_viaje";
    private static final String KEY_IDIOMA = "idioma";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    /**
     * Constructor
     */
    public PreferencesHelper(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    // ========== SESIÓN DE USUARIO ==========

    /**
     * Guarda el estado de login del usuario
     */
    public void setUserLogged(boolean logged, String username) {
        editor.putBoolean(KEY_USER_LOGGED, logged);
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    /**
     * Verifica si el usuario está logueado
     */
    public boolean isUserLogged() {
        return preferences.getBoolean(KEY_USER_LOGGED, false);
    }

    /**
     * Obtiene el nombre de usuario actual
     */
    public String getUsername() {
        return preferences.getString(KEY_USERNAME, "Usuario");
    }

    /**
     * Cierra la sesión del usuario
     */
    public void logout() {
        editor.putBoolean(KEY_USER_LOGGED, false);
        editor.putString(KEY_USERNAME, null);
        editor.apply();
    }

    // ========== PRIMERA VEZ ==========

    /**
     * Verifica si es la primera vez que se abre la app
     */
    public boolean isFirstTime() {
        return preferences.getBoolean(KEY_FIRST_TIME, true);
    }

    /**
     * Marca que la app ya no se está abriendo por primera vez
     */
    public void setFirstTime(boolean firstTime) {
        editor.putBoolean(KEY_FIRST_TIME, firstTime);
        editor.apply();
    }

    // ========== NOTIFICACIONES ==========

    /**
     * Habilita/deshabilita notificaciones diarias
     */
    public void setNotificacionesDiarias(boolean habilitadas) {
        editor.putBoolean(KEY_NOTIF_DIARIAS, habilitadas);
        editor.apply();
    }

    /**
     * Verifica si las notificaciones diarias están habilitadas
     */
    public boolean isNotificacionesDiariasHabilitadas() {
        return preferences.getBoolean(KEY_NOTIF_DIARIAS, true);
    }

    /**
     * Habilita/deshabilita alertas de viaje
     */
    public void setAlertasViaje(boolean habilitadas) {
        editor.putBoolean(KEY_ALERTAS_VIAJE, habilitadas);
        editor.apply();
    }

    /**
     * Verifica si las alertas de viaje están habilitadas
     */
    public boolean isAlertasViajeHabilitadas() {
        return preferences.getBoolean(KEY_ALERTAS_VIAJE, true);
    }

    // ========== IDIOMA ==========

    /**
     * Guarda el idioma seleccionado
     */
    public void setIdioma(String idioma) {
        editor.putString(KEY_IDIOMA, idioma);
        editor.apply();
    }

    /**
     * Obtiene el idioma seleccionado
     */
    public String getIdioma() {
        return preferences.getString(KEY_IDIOMA, "Español");
    }

    // ========== FAVORITOS (Métodos auxiliares) ==========

    /**
     * Guarda un favorito simple (alternativa ligera a SQLite)
     */
    public void agregarFavoritoSimple(String nombreLugar) {
        String favoritos = preferences.getString("favoritos_list", "");
        if (!favoritos.contains(nombreLugar)) {
            favoritos += nombreLugar + ",";
            editor.putString("favoritos_list", favoritos);
            editor.apply();
        }
    }

    /**
     * Elimina un favorito simple
     */
    public void eliminarFavoritoSimple(String nombreLugar) {
        String favoritos = preferences.getString("favoritos_list", "");
        favoritos = favoritos.replace(nombreLugar + ",", "");
        editor.putString("favoritos_list", favoritos);
        editor.apply();
    }

    /**
     * Verifica si un lugar está en favoritos (versión simple)
     */
    public boolean esFavoritoSimple(String nombreLugar) {
        String favoritos = preferences.getString("favoritos_list", "");
        return favoritos.contains(nombreLugar);
    }

    // ========== LIMPIEZA ==========

    /**
     * Limpia todas las preferencias (útil para testing o reset)
     */
    public void limpiarTodo() {
        editor.clear();
        editor.apply();
    }

    /**
     * Limpia solo las preferencias de usuario (mantiene configuración)
     */
    public void limpiarSesion() {
        editor.remove(KEY_USER_LOGGED);
        editor.remove(KEY_USERNAME);
        editor.apply();
    }
}