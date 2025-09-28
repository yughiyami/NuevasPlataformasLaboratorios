package com.example.broadcastbatery2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

public class BatteryStatusReceiver2 extends BroadcastReceiver {
    private static final String TAG = "BatteryReceiver2";
    private static final String BATTERY_CHECK_ACTION = "com.example.batterymonitorpending.BATTERY_CHECK";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action != null) {
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                // Actualización normal del estado de batería
                processBatteryInfo(intent);

            } else if (action.equals(BATTERY_CHECK_ACTION)) {
                // Chequeo periódico iniciado por PendingIntent
                Log.d(TAG, "Chequeo periódico de batería iniciado por PendingIntent");

                // Obtener el estado actual de la batería
                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = context.registerReceiver(null, ifilter);

                if (batteryStatus != null) {
                    processBatteryInfo(batteryStatus);

                    // Mostrar notificación Toast para indicar el chequeo periódico
                    int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    float batteryPct = level * 100 / (float) scale;

                    Toast.makeText(context,
                            "Chequeo periódico: Batería al " + (int)batteryPct + "%",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void processBatteryInfo(Intent intent) {
        // Obtener información de la batería
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
        int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        String technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);

        // Calcular el porcentaje
        float batteryPct = level * 100 / (float) scale;

        // Log para debugging
        Log.d(TAG, "Nivel de batería actualizado: " + (int)batteryPct + "%");
        Log.d(TAG, "Estado: " + getStatusString(status));
        Log.d(TAG, "Salud: " + getHealthString(health));
        Log.d(TAG, "Temperatura: " + (temperature / 10.0f) + "°C");
        Log.d(TAG, "Voltaje: " + (voltage / 1000.0f) + "V");

        // Actualizar la UI si MainActivity está activa
        MainActivity activity = MainActivity.getInstance();
        if (activity != null) {
            activity.runOnUiThread(() -> {
                MainActivity.updateBatteryLevel((int)batteryPct);
                MainActivity.updateBatteryStatus(getStatusString(status));
                MainActivity.updateBatteryHealth(getHealthString(health));
                MainActivity.updateBatteryTemperature(temperature / 10.0f);
                MainActivity.updateBatteryVoltage(voltage / 1000.0f);
                MainActivity.updateBatteryTechnology(technology != null ? technology : "Desconocida");
            });
        }
    }

    private String getStatusString(int status) {
        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                return "Cargando";
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                return "Descargando";
            case BatteryManager.BATTERY_STATUS_FULL:
                return "Completa";
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                return "No cargando";
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                return "Desconocido";
            default:
                return "Desconocido";
        }
    }

    private String getHealthString(int health) {
        switch (health) {
            case BatteryManager.BATTERY_HEALTH_GOOD:
                return "Buena";
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                return "Sobrecalentada";
            case BatteryManager.BATTERY_HEALTH_DEAD:
                return "Muerta";
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                return "Sobrevoltaje";
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                return "Fallo no especificado";
            case BatteryManager.BATTERY_HEALTH_COLD:
                return "Fría";
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                return "Desconocida";
            default:
                return "Desconocida";
        }
    }
}