package com.example.setting2;
//메인액티비티
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

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
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static TextView pushtext;
    public static Switch push;
    public static boolean isChecked;

    public static TextView env;
    public static TextView goalnum;
    public static TextView notice;
    public static AlarmManager alarmManager;
    public static PendingIntent pendingIntent;
    public static Calendar calendar;
    public static int hour, minute;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pushtext = findViewById(R.id.pushtext);
        push = findViewById(R.id.push);
        notice = findViewById(R.id.notice);
        env = findViewById(R.id.env);
        goalnum = findViewById(R.id.goalnum);

        //스위치 작동 관련
        push.setOnCheckedChangeListener(new pushListener());
//        createNotificationChannel();
//        alarmBroadcastReceiver();


        //환경 목표 횟수 변화 등 메뉴 클릭 시 화면 전환
        env.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), 해당 화면 클래스이름);
                //startActivityForResult(intent, );
            }
        });
        goalnum.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                
            }
        });


    }

    public void alarmBroadcastReceiver(){
        Intent alarmBroadcastReceiverIntent = new Intent(this, AlarmBroadCastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,alarmBroadcastReceiverIntent,0);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        calendar = Calendar.getInstance();
//
//        calendar.setTimeInMillis(System.currentTimeMillis());
//
//
//        calendar.set(Calendar.HOUR_OF_DAY, 14);
//
//
//        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            hour = 10;
            minute = 0;
        }

        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        Date today = new Date();
        long intervalDay = 6*60*60*1000; //6시간

        long selectTime = calendar.getTimeInMillis();
        long currentTime = System.currentTimeMillis();

        if(currentTime>selectTime)
            selectTime+=intervalDay;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, selectTime, intervalDay,pendingIntent);

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

    public void unregist(){ //알림설정 해제
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmBroadcastReceiverIntent = new Intent(this, AlarmBroadCastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,alarmBroadcastReceiverIntent,0);
        alarmManager.cancel(pendingIntent);
    }

    class pushListener implements CompoundButton.OnCheckedChangeListener {


        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            isChecked = push.isChecked();

            if (!isChecked) { //off 체크
                String disabledcolor = "#AEAEAE";
                unregist();
                //alarmManager.cancel(pendingIntent);
                pushtext.setTextColor(Color.parseColor(disabledcolor));
                notice.setTextColor(Color.parseColor(disabledcolor));


            }
            else { //on 체크
                createNotificationChannel();
//                if(calendar.HOUR_OF_DAY==4) //새벽 4시에는 알림 끄기
//                    alarmManager.cancel(pendingIntent);
//                else
                    alarmBroadcastReceiver();

                //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_DAY, pendingIntent); // Remember to change the time to a new time in millis.
                String enabledcolor = "#000000";
                String darkred = "#FFCC0000";
                pushtext.setTextColor(Color.parseColor(enabledcolor));
                notice.setTextColor(Color.parseColor(darkred));


            }

        }
    }


}