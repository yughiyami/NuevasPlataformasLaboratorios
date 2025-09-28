package com.example.broadcastreceptor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class MoodBroadcastReceiver extends BroadcastReceiver {

    public static final  String EXTRA_MOON_PHASE = "com.example.broadcastreceptor.MoodBroadcastReceiver.EXTRA_MOON_PHASE";

    private static  final  String TAG = "MoodBroadcastReceiver";

    private void  writeToFile (String data , Context context){
        try{
            OutputStreamWriter  outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch ( IOException e){
            Log.e("Exception","File write Failed: "+e.toString());

        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra(EXTRA_MOON_PHASE);
        writeToFile(message,context);
        Log.d(TAG , message);
    }
}
