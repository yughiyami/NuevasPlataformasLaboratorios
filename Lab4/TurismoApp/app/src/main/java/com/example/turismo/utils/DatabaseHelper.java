package com.example.turismo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.turismo.models.Lugar;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseHelper - Manejo de base de datos SQLite local
 * Permite guardar favoritos, historial, y datos offline
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Información de la Base de Datos
    private static final String DATABASE_NAME = "turismo.db";
    private static final int DATABASE_VERSION = 1;

    // Tabla: Favoritos
    private static final String TABLE_FAVORITOS = "favoritos";
    private static final String COL_ID = "id";
    private static final String COL_NOMBRE = "nombre";
    private static final String COL_DESCRIPCION = "descripcion";
    private static final String COL_PRECIO = "precio";
    private static final String COL_CATEGORIAS = "categorias";
    private static final String COL_ID_IMAGEN = "id_imagen";
    private static final String COL_FECHA_AGREGADO = "fecha_agregado";

    /**
     * Constructor
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de Favoritos
        String createTableFavoritos = "CREATE TABLE " + TABLE_FAVORITOS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOMBRE + " TEXT NOT NULL, " +
                COL_DESCRIPCION + " TEXT, " +
                COL_PRECIO + " REAL, " +
                COL_CATEGORIAS + " TEXT, " +
                COL_ID_IMAGEN + " INTEGER, " +
                COL_FECHA_AGREGADO + " TEXT" +
                ")";
        db.execSQL(createTableFavoritos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar tabla antigua y crear nueva
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITOS);
        onCreate(db);
    }

    /**
     * Agrega un lugar a favoritos
     */
    public boolean agregarFavorito(Lugar lugar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NOMBRE, lugar.getNombre());
        values.put(COL_DESCRIPCION, lugar.getDescripcion());
        values.put(COL_PRECIO, lugar.getPrecio());
        values.put(COL_CATEGORIAS, lugar.getCategorias());
        values.put(COL_ID_IMAGEN, lugar.getIdImagen());
        values.put(COL_FECHA_AGREGADO, System.currentTimeMillis());

        long resultado = db.insert(TABLE_FAVORITOS, null, values);
        db.close();

        return resultado != -1;
    }

    /**
     * Elimina un lugar de favoritos
     */
    public boolean eliminarFavorito(String nombreLugar) {
        SQLiteDatabase db = this.getWritableDatabase();
        int resultado = db.delete(TABLE_FAVORITOS,
                COL_NOMBRE + " = ?",
                new String[]{nombreLugar});
        db.close();

        return resultado > 0;
    }

    /**
     * Verifica si un lugar está en favoritos
     */
    public boolean esFavorito(String nombreLugar) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITOS,
                new String[]{COL_ID},
                COL_NOMBRE + " = ?",
                new String[]{nombreLugar},
                null, null, null);

        boolean existe = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return existe;
    }

    /**
     * Obtiene todos los lugares favoritos
     */
    public List<Lugar> obtenerFavoritos() {
        List<Lugar> listaFavoritos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVORITOS,
                null, null, null, null, null,
                COL_FECHA_AGREGADO + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Lugar lugar = new Lugar();
                lugar.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
                lugar.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE)));
                lugar.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPCION)));
                lugar.setPrecio(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRECIO)));
                lugar.setCategorias(cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORIAS)));
                lugar.setIdImagen(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_IMAGEN)));
                lugar.setEsFavorito(true);

                listaFavoritos.add(lugar);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listaFavoritos;
    }

    /**
     * Obtiene el número de favoritos
     */
    public int contarFavoritos() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_FAVORITOS, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count;
    }

    /**
     * Elimina todos los favoritos
     */
    public void limpiarFavoritos() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITOS, null, null);
        db.close();
    }

    /**
     * Busca favoritos por categoría
     */
    public List<Lugar> buscarFavoritosPorCategoria(String categoria) {
        List<Lugar> listaFavoritos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVORITOS,
                null,
                COL_CATEGORIAS + " LIKE ?",
                new String[]{"%" + categoria + "%"},
                null, null,
                COL_FECHA_AGREGADO + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Lugar lugar = new Lugar();
                lugar.setNombre(cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE)));
                lugar.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPCION)));
                lugar.setPrecio(cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRECIO)));
                lugar.setCategorias(cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORIAS)));
                lugar.setIdImagen(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID_IMAGEN)));
                lugar.setEsFavorito(true);

                listaFavoritos.add(lugar);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listaFavoritos;
    }
}