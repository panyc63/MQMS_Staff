package com.example.mqms_staff.Adapter;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.mqms_staff.Login_Page;
import com.example.mqms_staff.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyMessagingService extends FirebaseMessagingService {

    String TAG = "Firestore";


    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("userPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token",token);
        editor.apply();
        Log.d("Firestore",token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // playing audio and vibration when user se reques
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setLooping(false);
        }
        setVibrate();

        Log.d(TAG, "Notification: " + remoteMessage.getFrom());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CreateNotificationChannel();
        }
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            Log.d(TAG, "Title: " + title + " ------ " + "Body: " + body);

            showNotification(title, body);

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateNotificationChannel(){
        NotificationChannel notificationChannel = new NotificationChannel("channel_id","Test Notification",NotificationManager.IMPORTANCE_HIGH);

        notificationChannel.setDescription("Its your queue number");

        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
    }

    private void showNotification(String title,String body){
        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, Login_Page.class);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"channel_id")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(),0,intent, PendingIntent.FLAG_UPDATE_CURRENT));

        notificationManager.notify(1,builder.build());
    }

    private void setVibrate(){
        // vibration
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 300, 300, 300};
        v.vibrate(pattern, -1);

    }

}

