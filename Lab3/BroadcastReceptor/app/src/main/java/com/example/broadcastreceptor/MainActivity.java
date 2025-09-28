package com.example.broadcastreceptor;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.os.Build;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static  final  String  TAG = "Receptor";

    private MoodBroadcastReceiver moodBroadcastReceiver = new MoodBroadcastReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG , "onCreate");
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG,"onStart");
        IntentFilter filter = new IntentFilter(MoodBroadcastReceiver.EXTRA_MOON_PHASE);

        // Add the appropriate flag based on your Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // TIRAMISU is API 33
            registerReceiver(moodBroadcastReceiver, filter, RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(moodBroadcastReceiver, filter);
        }
    }

    @Override
    public  void onRestart(){
        Log.d(TAG,"onRestart");
        super.onRestart();
    }

    @Override
    public  void onResume(){
        Log.d(TAG,"onResume");
        super.onResume();
    }

    @Override
    public  void onPause(){
        Log.d(TAG,"onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
        unregisterReceiver(moodBroadcastReceiver); // Unregister here
    }

    @Override
    public  void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        // It's also a good practice to unregister in onDestroy as a fallback,
        // though onStop is generally preferred for paired registration/unregistration.
        // try-catch block is good practice here in case it was already unregistered.
        try {
            unregisterReceiver(moodBroadcastReceiver);
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "Receiver not registered or already unregistered: " + e.getMessage());
        }
    }
}