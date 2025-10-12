package com.example.broadcastbatery1;

import android.content.IntentFilter;
import android.os.Bundle;
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

    private static final String TAG = "BatteryMonitor";
    private TextView tvBatteryLevel;
    private TextView tvBatteryStatus;
    private TextView tvBatteryHealth;
    private TextView tvBatteryTemperature;
    private TextView tvBatteryVoltage;
    private TextView tvBatteryTechnology;
    private ProgressBar progressBar;
    private ImageView ivBatteryIcon;

    private BatteryStatusReceiver batteryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initializeViews();
        batteryReceiver = new BatteryStatusReceiver(this);


        /**
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        **/
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

        // Registrar el BroadcastReceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(android.content.Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);

        // Imprimir mensaje en consola
        Log.d(TAG, "BroadcastReceiver registrado satisfactoriamente");
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Desregistrar el BroadcastReceiver
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);

            // Imprimir mensaje en consola
            Log.d(TAG, "BroadcastReceiver desregistrado satisfactoriamente");
        }
    }
    public void updateBatteryLevel(int level) {
        tvBatteryLevel.setText("Nivel: " + level + "%");
        progressBar.setProgress(level);
        updateBatteryIcon(level);
    }

    public void updateBatteryStatus(String status) {
        tvBatteryStatus.setText("Estado: " + status);
    }

    public void updateBatteryHealth(String health) {
        tvBatteryHealth.setText("Salud: " + health);
    }

    public void updateBatteryTemperature(float temperature) {
        tvBatteryTemperature.setText("Temperatura: " + temperature + "°C");
    }

    public void updateBatteryVoltage(float voltage) {
        tvBatteryVoltage.setText("Voltaje: " + voltage + "V");
    }

    public void updateBatteryTechnology(String technology) {
        tvBatteryTechnology.setText("Tecnología: " + technology);
    }

    private void updateBatteryIcon(int level) {
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