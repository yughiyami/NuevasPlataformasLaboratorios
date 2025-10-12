package com.example.broadcastbatery1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

public class BatteryStatusReceiver extends BroadcastReceiver {
    private static final String TAG = "BatteryStatusReceiver";
    private MainActivity mainActivity;

    // Constructor
    public BatteryStatusReceiver(MainActivity activity) {
        this.mainActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null &&
                intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {

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
            Log.d(TAG, "Nivel de batería: " + (int)batteryPct + "%");

            // Actualizar la UI a través de MainActivity
            if (mainActivity != null) {
                mainActivity.updateBatteryLevel((int)batteryPct);
                mainActivity.updateBatteryStatus(getStatusString(status));
                mainActivity.updateBatteryHealth(getHealthString(health));
                mainActivity.updateBatteryTemperature(temperature / 10.0f);
                mainActivity.updateBatteryVoltage(voltage / 1000.0f);
                mainActivity.updateBatteryTechnology(technology != null ? technology : "Desconocida");
            }
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