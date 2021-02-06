package com.example.setting2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;


import java.util.Calendar;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    public TextView pushtext;
    public Switch push;
    public boolean isChecked;
    public TextView intervaltext;
    public String items[] = {"", "3시간", "4시간"};
    public TextView interrupttext;
    public String items1[] = {"", "23시", "00시"};
    public TextView to;
    public String items2[] = {"", "6시", "7시"};
    public TextView env;
    public TextView goalnum;
    public Spinner spinner;
    public Spinner spinner1;
    public Spinner spinner2;
    public static AlarmManager alarmManager;
    public static PendingIntent pendingIntent;
    public static Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pushtext = findViewById(R.id.pushtext);
        push = findViewById(R.id.push);
        intervaltext = findViewById(R.id.intervaltext);
        interrupttext = findViewById(R.id.interrupttext);
        to = findViewById(R.id.to);
        env = findViewById(R.id.env);
        goalnum = findViewById(R.id.goalnum);

        //스위치 작동 관련
        push.setOnCheckedChangeListener(new pushListener());
        createNotificationChannel();
        alarmBroadcastReceiver();

        //스피너 작동 관련
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner1 = findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void alarmBroadcastReceiver(){
            Intent alarmBroadcastReceiverIntent = new Intent(this, AlarmBroadCastReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, alarmBroadcastReceiverIntent, 0);

            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis()); //지금 시간으로 기준 설정?
            calendar.add(Calendar.MINUTE, 1);

            //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),20*1000, pendingIntent);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    public void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            CharSequence name = "알림설정에서의 제목";
            String description = "Oreo Version 이상을 위한 알림(알림설정에서의 설명)";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    class pushListener implements CompoundButton.OnCheckedChangeListener {


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            isChecked = push.isChecked();

            if (!isChecked) {
                String disabledcolor = "#AEAEAE";
                alarmManager.cancel(pendingIntent);
                pushtext.setTextColor(Color.parseColor(disabledcolor));
                intervaltext.setTextColor(Color.parseColor(disabledcolor));
                interrupttext.setTextColor(Color.parseColor(disabledcolor));
                to.setTextColor(Color.parseColor(disabledcolor));
                spinner.setEnabled(false);
                spinner1.setEnabled(false);
                spinner2.setEnabled(false);
                spinner.setSelection(0);
                spinner1.setSelection(0);
                spinner2.setSelection(0);

            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent); // Remember to change the time to a new time in millis.
                String enabledcolor = "#000000";
                pushtext.setTextColor(Color.parseColor(enabledcolor));
                intervaltext.setTextColor(Color.parseColor(enabledcolor));
                interrupttext.setTextColor(Color.parseColor(enabledcolor));
                to.setTextColor(Color.parseColor(enabledcolor));
                spinner.setEnabled(true);
                spinner1.setEnabled(true);
                spinner2.setEnabled(true);

            }

        }
    }
}