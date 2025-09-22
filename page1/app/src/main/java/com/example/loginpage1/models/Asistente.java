package com.example.loginpage1.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Asistente {
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String grupoSanguineo;
    private String organizacion;
    private String areaInteres;
    private String fechaRegistro;

    // Constructor vacío
    public Asistente() {
        this.fechaRegistro = getCurrentDate();
    }

    // Constructor con parámetros
    public Asistente(String nombres, String apellidos, String correo, String telefono,
                     String grupoSanguineo, String organizacion, String areaInteres) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.grupoSanguineo = grupoSanguineo;
        this.organizacion = organizacion;
        this.areaInteres = areaInteres;
        this.fechaRegistro = getCurrentDate();
    }

    // Getters
    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public String getOrganizacion() {
        return organizacion;
    }

    public String getAreaInteres() {
        return areaInteres;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    // Setters
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setGrupoSanguineo(String grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public void setOrganizacion(String organizacion) {
        this.organizacion = organizacion;
    }

    public void setAreaInteres(String areaInteres) {
        this.areaInteres = areaInteres;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // Método para obtener la fecha actual
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Método para validar los datos
    public boolean isValid() {
        return nombres != null && !nombres.trim().isEmpty() &&
                apellidos != null && !apellidos.trim().isEmpty() &&
                correo != null && !correo.trim().isEmpty() &&
                telefono != null && !telefono.trim().isEmpty() &&
                grupoSanguineo != null && !grupoSanguineo.trim().isEmpty();
    }

    // Método toString para mostrar en consola
    @Override
    public String toString() {
        return "======= ASISTENTE REGISTRADO =======\n" +
                "Nombres: " + nombres + "\n" +
                "Apellidos: " + apellidos + "\n" +
                "Correo: " + correo + "\n" +
                "Teléfono: " + telefono + "\n" +
                "Grupo Sanguíneo: " + grupoSanguineo + "\n" +
                "Organización: " + organizacion + "\n" +
                "Área de Interés: " + areaInteres + "\n" +
                "Fecha de Registro: " + fechaRegistro + "\n" +
                "===================================\n";
    }

    // Método para convertir a formato de archivo de texto
    public String toFileFormat() {
        return nombres + "|" + apellidos + "|" + correo + "|" + telefono + "|" +
                grupoSanguineo + "|" + organizacion + "|" + areaInteres + "|" + fechaRegistro;
    }

    // Método estático para crear Asistente desde formato de archivo
    public static Asistente fromFileFormat(String linea) {
        String[] datos = linea.split("\\|");
        if (datos.length >= 7) {
            Asistente asistente = new Asistente();
            asistente.setNombres(datos[0]);
            asistente.setApellidos(datos[1]);
            asistente.setCorreo(datos[2]);
            asistente.setTelefono(datos[3]);
            asistente.setGrupoSanguineo(datos[4]);
            asistente.setOrganizacion(datos[5]);
            asistente.setAreaInteres(datos[6]);
            if (datos.length >= 8) {
                asistente.setFechaRegistro(datos[7]);
            }
            return asistente;
        }
        return null;
    }

    // Método para obtener nombre completo
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}