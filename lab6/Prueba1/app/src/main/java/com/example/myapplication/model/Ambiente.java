package com.example.myapplication.model;

import android.graphics.PointF;
import java.util.List;

public class Ambiente {
    private String id;
    private String nombre;
    private String tipo; // "salon" o "patio"
    private String descripcion;
    private float area;
    private List<PointF> vertices;
    private boolean esCircular;
    private PointF centro;
    private float radio;

    public Ambiente() {
    }

    public Ambiente(String id, String nombre, String tipo, String descripcion,
                    float area, List<PointF> vertices) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.area = area;
        this.vertices = vertices;
        this.esCircular = false;
    }

    public Ambiente(String id, String nombre, String tipo, String descripcion,
                    float area, PointF centro, float radio) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.area = area;
        this.centro = centro;
        this.radio = radio;
        this.esCircular = true;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public List<PointF> getVertices() {
        return vertices;
    }

    public void setVertices(List<PointF> vertices) {
        this.vertices = vertices;
    }

    public boolean isEsCircular() {
        return esCircular;
    }

    public void setEsCircular(boolean esCircular) {
        this.esCircular = esCircular;
    }

    public PointF getCentro() {
        return centro;
    }

    public void setCentro(PointF centro) {
        this.centro = centro;
    }

    public float getRadio() {
        return radio;
    }

    public void setRadio(float radio) {
        this.radio = radio;
    }

    // Método para verificar si un punto está dentro del ambiente
    public boolean contienePunto(float x, float y) {
        if (esCircular) {
            float dx = x - centro.x;
            float dy = y - centro.y;
            return (dx * dx + dy * dy) <= (radio * radio);
        } else {
            return puntoDentroPoligono(x, y, vertices);
        }
    }

    // Algoritmo Ray Casting para determinar si un punto está dentro de un polígono
    private boolean puntoDentroPoligono(float x, float y, List<PointF> vertices) {
        int intersecciones = 0;
        int numVertices = vertices.size();

        for (int i = 0; i < numVertices; i++) {
            PointF v1 = vertices.get(i);
            PointF v2 = vertices.get((i + 1) % numVertices);

            if ((v1.y > y) != (v2.y > y)) {
                float xInterseccion = (v2.x - v1.x) * (y - v1.y) / (v2.y - v1.y) + v1.x;
                if (x < xInterseccion) {
                    intersecciones++;
                }
            }
        }

        return (intersecciones % 2) == 1;
    }

    // Método para obtener el centro del ambiente (útil para posicionar etiquetas)
    public PointF getCentroAmbiente() {
        if (esCircular) {
            return centro;
        } else {
            float sumX = 0;
            float sumY = 0;
            for (PointF vertex : vertices) {
                sumX += vertex.x;
                sumY += vertex.y;
            }
            return new PointF(sumX / vertices.size(), sumY / vertices.size());
        }
    }
}