package com.example.alarmmanager_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alarmmanager_demo.databinding.ActivityMainBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MaterialTimePicker picker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private EditText inputMedicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //first we create notification channel
        createNotificationChannel();
       String MedicineName =binding.inputMedicine.getText().toString();

        binding.BtnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             showTimePicker();
            }
        });

        binding.BtnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();

            }
        });

        binding.BtnCancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               cancelAlarm();
            }
        });
    }

    private void cancelAlarm() {
        Intent intent=new Intent(this,AlarmReceiver.class);
        pendingIntent=PendingIntent.getBroadcast(this,0,intent,0);
        if (alarmManager==null)
        {
            alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm cancelled", Toast.LENGTH_SHORT).show();
    }

    private void setAlarm() {
      alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
      Intent intent=new Intent(this,AlarmReceiver.class);
      pendingIntent=PendingIntent.getBroadcast(this,0,intent,0);
      alarmManager.setInexactRepeating(AlarmManager.RTC,calendar.getTimeInMillis(),
              AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(this, "Alarm set successfully!", Toast.LENGTH_SHORT).show();

    }

    private void showTimePicker() {
            picker=new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("selected time Alarm")
                    .build();
            picker.show(getSupportFragmentManager(),"foxandroid");
            picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onClick(View v) {
                    if (picker.getHour()>=12){
                        binding.textviewDisplayTime.setText(String.format("%02d",(picker.getHour()-12)) +":"+String.format("%02d",picker.getMinute()) + "PM");
                    }
                    else
                    {
                        binding.textviewDisplayTime.setText(String.format("%02d",(picker.getHour())) +":"+ String.format("%02d",picker.getMinute()) + "PM");
                    }
                    calendar= Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY,picker.getHour());
                    calendar.set(Calendar.MINUTE,picker.getMinute());
                    calendar.set(Calendar.SECOND,0);
                    calendar.set(Calendar.MILLISECOND,0);
                }
            });
    }

    private void createNotificationChannel() {
        //if your app runnning in android
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O)
        {

            CharSequence name="FoxAndroidReminderChannel";
            String description="channel For Alarm manager";
            int importance= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel("foxandroid",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
}