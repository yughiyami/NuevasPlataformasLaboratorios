package com.example.turismo.models;

import java.io.Serializable;

/**
 * Clase Lugar - Modelo de datos para lugares turísticos
 * Implementa Serializable para poder pasar objetos entre Activities
 */
public class Lugar implements Serializable {

    // Atributos
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String categorias;
    private int idImagen; // Recurso drawable
    private String urlImagen; // URL para cargar desde internet
    private String detallesCompletos;
    private double latitud;
    private double longitud;
    private boolean esFavorito;
    private String horarioAtencion;
    private float valoracion; // Rating de 0 a 5

    // Constructores

    /**
     * Constructor completo
     */
    public Lugar(int id, String nombre, String descripcion, double precio,
                 String categorias, int idImagen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categorias = categorias;
        this.idImagen = idImagen;
        this.esFavorito = false;
        this.valoracion = 0.0f;
    }

    /**
     * Constructor simplificado
     */
    public Lugar(String nombre, String descripcion, double precio,
                 String categorias, int idImagen) {
        this(0, nombre, descripcion, precio, categorias, idImagen);
    }

    /**
     * Constructor vacío
     */
    public Lugar() {
        this.esFavorito = false;
        this.valoracion = 0.0f;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCategorias() {
        return categorias;
    }

    public void setCategorias(String categorias) {
        this.categorias = categorias;
    }

    public int getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getDetallesCompletos() {
        if (detallesCompletos == null || detallesCompletos.isEmpty()) {
            return "Acerca de " + nombre + ":\n\n" +
                    descripcion + "\n\n" +
                    "Este es un lugar imperdible para visitar. " +
                    "Cuenta con características únicas que lo hacen especial.";
        }
        return detallesCompletos;
    }

    public void setDetallesCompletos(String detallesCompletos) {
        this.detallesCompletos = detallesCompletos;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public boolean isEsFavorito() {
        return esFavorito;
    }

    public void setEsFavorito(boolean esFavorito) {
        this.esFavorito = esFavorito;
    }

    public String getHorarioAtencion() {
        return horarioAtencion;
    }

    public void setHorarioAtencion(String horarioAtencion) {
        this.horarioAtencion = horarioAtencion;
    }

    public float getValoracion() {
        return valoracion;
    }

    public void setValoracion(float valoracion) {
        this.valoracion = valoracion;
    }

    // Métodos útiles

    /**
     * Obtiene el precio formateado con moneda
     */
    public String getPrecioFormateado() {
        if (precio == 0) {
            return "Gratis";
        }
        return "S/ " + String.format("%.2f", precio);
    }

    /**
     * Verifica si el lugar pertenece a una categoría específica
     */
    public boolean perteneceACategoria(String categoria) {
        return categorias != null &&
                categorias.toLowerCase().contains(categoria.toLowerCase());
    }

    @Override
    public String toString() {
        return "Lugar{" +
                "nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", categorias='" + categorias + '\'' +
                '}';
    }
}