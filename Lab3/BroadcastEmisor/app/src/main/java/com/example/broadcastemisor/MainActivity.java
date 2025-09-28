package com.example.broadcastemisor;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private  static  final  String TAG = "Emisor";

    public static  final  String EXTRA_MOON_PHASE = "com.example.broadcastreceptor.MoonBroadcastReceiver.EXTRA_MOON.PHASE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button btnSenMessage = findViewById(R.id.button);
        EditText editMessage = findViewById(R.id.editTextText);

        btnSenMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editMessage.getText().toString();
                sendMessage(message);
            }
        });

    }

    public  void sendMessage(String message){
        Intent intent =new Intent();
        intent.setComponent(new ComponentName("com.example.broadcastreceptor","com.example.broadcastreceptor.MoonBroadcastReceiver"));

        intent.setAction(EXTRA_MOON_PHASE);
        intent.putExtra(EXTRA_MOON_PHASE,message);

        sendBroadcast(intent);
        Log.d(TAG,"mensage enviado");

    }
}