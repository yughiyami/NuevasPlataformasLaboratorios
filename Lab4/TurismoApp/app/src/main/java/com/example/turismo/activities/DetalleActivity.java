package com.example.turismo.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.turismo.R;
import com.example.turismo.models.Lugar;

/**
 * DetalleActivity - Muestra información detallada de un lugar turístico
 * Permite reservar, agregar a favoritos y compartir
 */
public class DetalleActivity extends AppCompatActivity {

    // Declaración de variables
    private ImageView ivImagen;
    private TextView tvNombre, tvDescripcion, tvPrecio, tvCategorias, tvDetalles;
    private Button btnReservar;
    private ImageButton btnVolver, btnFavorito, btnCompartir;

    private Lugar lugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        // Inicializar vistas
        inicializarVistas();

        // Obtener datos del Intent
        cargarDatos();

        // Configurar eventos
        configurarEventos();
    }

    /**
     * Inicializa todas las vistas del layout
     */
    private void inicializarVistas() {
        // ImageView
        ivImagen = findViewById(R.id.ivImagenDetalle);

        // TextViews
        tvNombre = findViewById(R.id.tvNombreDetalle);
        tvDescripcion = findViewById(R.id.tvDescripcionDetalle);
        tvPrecio = findViewById(R.id.tvPrecioDetalle);
        tvCategorias = findViewById(R.id.tvCategoriasDetalle);
        tvDetalles = findViewById(R.id.tvDetallesDetalle);

        // Botones
        btnReservar = findViewById(R.id.btnReservar);
        btnVolver = findViewById(R.id.btnVolver);
        btnFavorito = findViewById(R.id.btnFavorito);
        btnCompartir = findViewById(R.id.btnCompartir);
    }

    /**
     * Carga los datos recibidos desde el Intent
     */
    private void cargarDatos() {
        // Obtener el objeto Lugar del Intent
        lugar = (Lugar) getIntent().getSerializableExtra("LUGAR");

        if (lugar != null) {
            // Cargar datos en las vistas
            tvNombre.setText(lugar.getNombre());
            tvDescripcion.setText(lugar.getDescripcion());
            tvPrecio.setText("S/ " + lugar.getPrecio());
            tvCategorias.setText("Categorías: " + lugar.getCategorias());
            tvDetalles.setText(lugar.getDetallesCompletos());

            // Cargar imagen (usando Glide o Picasso en producción)
            // Glide.with(this).load(lugar.getUrlImagen()).into(ivImagen);
            ivImagen.setImageResource(lugar.getIdImagen());
        }
    }

    /**
     * Configura los eventos de click de los botones
     */
    private void configurarEventos() {
        // Botón Volver
        btnVolver.setOnClickListener(v -> finish());

        // Botón Reservar
        btnReservar.setOnClickListener(v -> realizarReserva());

        // Botón Favorito
        btnFavorito.setOnClickListener(v -> agregarAFavoritos());

        // Botón Compartir
        btnCompartir.setOnClickListener(v -> compartirLugar());
    }

    /**
     * Realiza el proceso de reserva
     */
    private void realizarReserva() {
        Toast.makeText(this,
                "Reservando " + lugar.getNombre() + " por S/ " + lugar.getPrecio(),
                Toast.LENGTH_SHORT).show();

        // Aquí iría la lógica de reserva real:
        // - Validar disponibilidad
        // - Procesar pago
        // - Guardar reserva en BD
        // - Enviar confirmación
    }

    /**
     * Agrega el lugar a favoritos
     */
    private void agregarAFavoritos() {
        // Guardar en base de datos local o SharedPreferences
        Toast.makeText(this, "Agregado a favoritos", Toast.LENGTH_SHORT).show();

        // Cambiar icono del botón para mostrar que ya está en favoritos
        btnFavorito.setImageResource(R.drawable.ic_favorite_filled);
    }

    /**
     * Comparte el lugar mediante Intent de Android
     */
    private void compartirLugar() {
        String textoCompartir = lugar.getNombre() + "\n" +
                lugar.getDescripcion() + "\n" +
                "Precio: S/ " + lugar.getPrecio();

        android.content.Intent sendIntent = new android.content.Intent();
        sendIntent.setAction(android.content.Intent.ACTION_SEND);
        sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, textoCompartir);
        sendIntent.setType("text/plain");

        startActivity(android.content.Intent.createChooser(sendIntent, "Compartir mediante"));
    }
}