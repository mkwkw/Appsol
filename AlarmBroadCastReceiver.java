package com.example.setting2;
//BroadcastReceiver 코드입니다.
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class AlarmBroadCastReceiver extends BroadcastReceiver {

    // public static Context context;

    public AlarmBroadCastReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent alarmIntentServiceIntent = new Intent(context, AlarmIntentService.class);
        context.startService(alarmIntentServiceIntent);

    }



}