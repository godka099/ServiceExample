package com.example.serviceexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn_start, btn_stop;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        //음악 백그라운드에서 실행
        btn_start = (Button)findViewById(R.id.btn_start);
        btn_stop = (Button)findViewById(R.id.btn_stop);

        btn_start.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startService(new Intent(getApplicationContext(), MusicService.class));
                createNotification();
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                stopService(new Intent(getApplicationContext(), MusicService.class));
                removeNotification();
            }
        });


    }

    private void createNotification() {
        //알림창 눌렀을대 나타낼 액티비티
        Intent intent= new Intent(this, MainActivity.class);
        //클릭할 때까지 액티비티 실행을 보류하고 있는 PendingIntent 객체 생성
        PendingIntent pending= PendingIntent.getActivity(this, 0
                , intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //stop 버튼 intent
        Intent intentHide = new Intent(this, StopServiceReceiver.class);
        PendingIntent hide = PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(), intentHide, PendingIntent.FLAG_CANCEL_CURRENT);




        //알림을 해줄 builder 선언
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("카나리앱");
        builder.setContentText("으로 A+가나");
        builder.setColor(Color.RED);
        builder.setContentIntent(pending);
        builder.setOngoing(true); //알림바 고정
        builder.addAction(R.drawable.ic_launcher_foreground, "stop", hide);


        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }

    public void removeNotification() {
        // Notification 제거
        NotificationManagerCompat.from(this).cancel(1);

    }

}
