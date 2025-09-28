package com.example.broadcastbatery2;

import android.os.Bundle;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "BatteryMonitorPending";
    private static final int REQUEST_CODE = 1001;

    // Views
    private static TextView tvBatteryLevel;
    private static TextView tvBatteryStatus;
    private static TextView tvBatteryHealth;
    private static TextView tvBatteryTemperature;
    private static TextView tvBatteryVoltage;
    private static TextView tvBatteryTechnology;
    private static ProgressBar progressBar;
    private static ImageView ivBatteryIcon;

    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private BatteryStatusReceiver2 batteryReceiver;

    // Instancia estática para acceso desde el receiver
    private static MainActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        instance = this;

        // Inicializar vistas
        initializeViews();

        // Inicializar AlarmManager
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Crear el receiver
        batteryReceiver = new BatteryStatusReceiver2();

    }

    private void initializeViews() {
        tvBatteryLevel = findViewById(R.id.tvBatteryLevel);
        tvBatteryStatus = findViewById(R.id.tvBatteryStatus);
        tvBatteryHealth = findViewById(R.id.tvBatteryHealth);
        tvBatteryTemperature = findViewById(R.id.tvBatteryTemperature);
        tvBatteryVoltage = findViewById(R.id.tvBatteryVoltage);
        tvBatteryTechnology = findViewById(R.id.tvBatteryTechnology);
        progressBar = findViewById(R.id.progressBar);
        ivBatteryIcon = findViewById(R.id.ivBatteryIcon);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Registrar el BroadcastReceiver para ACTION_BATTERY_CHANGED
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);

        Intent intent = new Intent(this, BatteryStatusReceiver2.class);
        intent.setAction("com.example.batterymonitorpending.BATTERY_CHECK");

        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        pendingIntent = PendingIntent.getBroadcast(
                this,
                REQUEST_CODE,
                intent,
                flags
        );

        // Configurar alarma repetitiva cada 30 segundos
        alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 1000,
                30000, // 30 segundos
                pendingIntent
        );

        // Imprimir mensaje en consola
        Log.d(TAG, "BroadcastReceiver con PendingIntent registrado satisfactoriamente");
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Cancelar la alarma
        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }

        // Desregistrar el receiver
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);

            // Imprimir mensaje en consola
            Log.d(TAG, "BroadcastReceiver con PendingIntent desregistrado satisfactoriamente");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    // Métodos estáticos para actualizar la UI desde el receiver
    public static void updateBatteryLevel(int level) {
        if (tvBatteryLevel != null) {
            tvBatteryLevel.setText("Nivel: " + level + "%");
            progressBar.setProgress(level);
            updateBatteryIcon(level);
        }
    }

    public static void updateBatteryStatus(String status) {
        if (tvBatteryStatus != null) {
            tvBatteryStatus.setText("Estado: " + status);
        }
    }

    public static void updateBatteryHealth(String health) {
        if (tvBatteryHealth != null) {
            tvBatteryHealth.setText("Salud: " + health);
        }
    }

    public static void updateBatteryTemperature(float temperature) {
        if (tvBatteryTemperature != null) {
            tvBatteryTemperature.setText("Temperatura: " + temperature + "°C");
        }
    }

    public static void updateBatteryVoltage(float voltage) {
        if (tvBatteryVoltage != null) {
            tvBatteryVoltage.setText("Voltaje: " + voltage + "V");
        }
    }

    public static void updateBatteryTechnology(String technology) {
        if (tvBatteryTechnology != null) {
            tvBatteryTechnology.setText("Tecnología: " + technology);
        }
    }

    private static void updateBatteryIcon(int level) {
        if (ivBatteryIcon != null) {
            if (level >= 75) {
                ivBatteryIcon.setImageResource(R.drawable.ic_battery_full);
            } else if (level >= 50) {
                ivBatteryIcon.setImageResource(R.drawable.ic_battery_75);
            } else if (level >= 25) {
                ivBatteryIcon.setImageResource(R.drawable.ic_battery_50);
            } else {
                ivBatteryIcon.setImageResource(R.drawable.ic_battery_25);
            }
        }
    }

    public static MainActivity getInstance() {
        return instance;
    }
}