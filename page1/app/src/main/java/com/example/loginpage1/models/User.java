package com.example.loginpage1.models;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class User {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String dateCreated;

    // Constructor vacío
    public User() {
        this.dateCreated = getCurrentDate();
    }

    // Constructor con parámetros
    public User(String username, String password, String email, String fullName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.dateCreated = getCurrentDate();
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    // Método para obtener la fecha actual
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Método para validar los datos
    public boolean isValid() {
        return username != null && !username.trim().isEmpty() &&
                password != null && !password.trim().isEmpty() &&
                email != null && !email.trim().isEmpty() &&
                fullName != null && !fullName.trim().isEmpty();
    }

    // Convertir a JSON
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("password", password);
            json.put("email", email);
            json.put("fullName", fullName);
            json.put("dateCreated", dateCreated);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    // Crear User desde JSON
    public static User fromJSON(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            User user = new User();
            user.setUsername(json.getString("username"));
            user.setPassword(json.getString("password"));
            user.setEmail(json.getString("email"));
            user.setFullName(json.getString("fullName"));
            user.setDateCreated(json.getString("dateCreated"));
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método toString para mostrar en consola
    @NonNull
    @Override
    public String toString() {
        return "======= USUARIO =======\n" +
                "Username: " + username + "\n" +
                "Nombre Completo: " + fullName + "\n" +
                "Email: " + email + "\n" +
                "Fecha de Creación: " + dateCreated + "\n" +
                "=======================\n";
    }

    // Método para verificar credenciales
    public boolean checkCredentials(String inputUsername, String inputPassword) {
        return this.username.equals(inputUsername) && this.password.equals(inputPassword);
    }
}