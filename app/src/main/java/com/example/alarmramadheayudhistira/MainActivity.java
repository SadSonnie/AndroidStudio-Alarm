package com.example.alarmramadheayudhistira;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View.OnClickListener;

import android.app.TimePickerDialog.OnTimeSetListener;
import android.media.MediaPlayer;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.TextView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.alarmramadheayudhistira.databinding.ActivityMainBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

    public class MainActivity extends AppCompatActivity {

        private ActivityMainBinding binding;
        private MaterialTimePicker picker;
        private Calendar calendar;
        private AlarmManager alarmManager;
        private PendingIntent pendingIntent;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createNotificationChannel();
        Button ext=findViewById(R.id.exit);
        Button stp=findViewById(R.id.Stop);


        binding.setwaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showTimePicker();

            }
        });

        binding.setalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setAlarm();

            }
        });

        binding.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelAlarm();



            }
        });

    }

        private void cancelAlarm() {

            Intent intent = new Intent(this,MyReceiver.class);

            pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);

            if (alarmManager == null){

                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            }

            alarmManager.cancel(pendingIntent);
            final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setIcon(R.drawable.ic_baseline_android_24);
            builder.setTitle("Confirmation Dialog");
            builder.setMessage("Apakah Anda Yakin untuk Keluar?");
            builder.setPositiveButton("Ya,Saya Ingin Keluar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    finish();

                }
            });
            builder.setNegativeButton("Tidak,Saya tidak ingin keluar",null);
            final AlertDialog alertDialog =builder.create();
            alertDialog.show();

        }
















        private void setAlarm() {

            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(this,MyReceiver.class);

            pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,pendingIntent);

            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Alert Dialog");
            alert.setMessage("Alarm Berhasil di Buat");
            alert.setPositiveButton("OK", null);
            alert.show();






        }

        private void showTimePicker() {

            picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Pilih Waktu")
                    .build();

            picker.show(getSupportFragmentManager(),"ramdhe");

            picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (picker.getHour() > 12){

                        binding.setwaktu.setText(
                                String.format("%02d",(picker.getHour()-12))+" : "+String.format("%02d",picker.getMinute())+" PM"
                        );

                    }else {

                        binding.setwaktu.setText(picker.getHour()+" : " + picker.getMinute() + " AM");

                    }

                    calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY,picker.getHour());
                    calendar.set(Calendar.MINUTE,picker.getMinute());
                    calendar.set(Calendar.SECOND,0);
                    calendar.set(Calendar.MILLISECOND,0);

                }
            });
            Toast.makeText(this, "Setelah berhasil Set Waktu,klik button 'Jalankan Alarm'Untuk mengaktifkan Alarm.", Toast.LENGTH_SHORT).show();


        }

        private void createNotificationChannel() {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                CharSequence name = "RamdheChannel";
                String description = "Alarm Coba-Coba";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel("ramdhe",name,importance);
                channel.setDescription(description);

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);

            }


        }

}