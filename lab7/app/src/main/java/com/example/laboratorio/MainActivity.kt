package com.example.laboratorio

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var rvEdificaciones: RecyclerView
    private lateinit var etBuscar: TextInputEditText
    private lateinit var adapter: EdificacionAdapter
    private val listaEdificaciones = mutableListOf<Edificacion>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inicializarVistas()
        cargarDatos()
        configurarRecyclerView()
        configurarBusqueda()
    }

    private fun inicializarVistas() {
        rvEdificaciones = findViewById(R.id.rvEdificaciones)
        etBuscar = findViewById(R.id.etBuscar)
    }

    private fun cargarDatos() {
        try {
            val inputStream = assets.open("edificaciones.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))

            reader.useLines { lines ->
                lines.forEach { linea ->
                    if (linea.isNotBlank()) {
                        val datos = linea.split("|")
                        if (datos.size >= 4) {
                            val edificacion = Edificacion(
                                id = datos[0].toInt(),
                                nombre = datos[1].trim(),
                                categoria = datos[2].trim(),
                                descripcion = datos[3].trim(),
                                imagenResId = obtenerImagenId(datos[0].toInt())
                            )
                            listaEdificaciones.add(edificacion)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            cargarDatosEjemplo()
        }
    }

    private fun cargarDatosEjemplo() {
        // Datos de ejemplo si no existe el archivo
        listaEdificaciones.addAll(
            listOf(
                Edificacion(1, "Catedral de Lima", "Iglesia",
                    "Principal templo católico del Perú, construcción colonial del siglo XVI",
                    R.drawable.catedral_de_lima),
                Edificacion(2, "Basílica de Arequipa", "Iglesia",
                    "Majestuosa construcción de sillar blanco en la Plaza de Armas",
                    R.drawable.basilica_arequipa),
                Edificacion(3, "Palacio de Gobierno", "Palacio",
                    "Sede del poder ejecutivo peruano, arquitectura neocolonial",
                    R.drawable.palacio_gobierno),
                Edificacion(4, "Casa de Aliaga", "Casa Colonial",
                    "Casa colonial más antigua de Lima, habitada por la misma familia",
                    R.drawable.casa_aliaga),
                Edificacion(5, "Monasterio de Santa Catalina", "Convento",
                    "Ciudadela colonial de 20,000 m² en Arequipa",
                    R.drawable.santa_catalina)
            )
        )
    }

    private fun obtenerImagenId(id: Int): Int {
        return when (id) {
            1 -> R.drawable.catedral_de_lima
            2 -> R.drawable.basilica_arequipa
            3 -> R.drawable.palacio_gobierno
            4 -> R.drawable.casa_aliaga
            5 -> R.drawable.santa_catalina
            else -> R.drawable.ic_placeholder
        }
    }

    private fun configurarRecyclerView() {
        adapter = EdificacionAdapter(listaEdificaciones)
        rvEdificaciones.layoutManager = LinearLayoutManager(this)
        rvEdificaciones.adapter = adapter
    }

    private fun configurarBusqueda() {
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}

