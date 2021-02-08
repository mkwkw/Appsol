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
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.CompoundButton;


import java.util.Calendar;

public class SettingMainActivity extends AppCompatActivity {

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
    public int interval=3; //기본 시간 간격
    public int start; //방해 금지 시작 시간
    public int end; //방해 금지 끝나는 시간


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity_main);

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

                if(position==1)
                    interval = 3;
                else if(position==2)
                    interval = 4;

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
                if(position==1)
                    start=23;
                else if(position==2)
                    start=24;
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
                if(position==1)
                    end=6;
                else if(position==2)
                    end=7;
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

            calendar.setTimeInMillis(System.currentTimeMillis());
            //calendar.set(Calendar.HOUR_OF_DAY, 11);
            //calendar.set(Calendar.MINUTE, 00);
            // calendar.set(Calendar.SECOND, 00);

            //calendar.set(Calendar.HOUR_OF_DAY, 14);
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

//            if(calendar.HOUR_OF_DAY>=start || calendar.HOUR_OF_DAY<=end)
//                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
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


            }
            else {
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