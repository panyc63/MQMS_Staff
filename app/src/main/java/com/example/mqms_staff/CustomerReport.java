package com.example.mqms_staff;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.mqms_staff.Adapter.FcmNotificationSender;
import com.example.mqms_staff.Classes.UserClass;
import com.example.mqms_staff.Classes.customer;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class CustomerReport extends AppCompatActivity {

    private String TAG = "Firestore";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CountDownTimer cTimer;
    private String queueNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        HashMap<String, String> queueMap = new HashMap<>();
        queueMap.put("GE","General Enquiries");
        queueMap.put("SE","Selective En bloc Redevelopment Scheme(SERS)");
        queueMap.put("SA","Sales Agreement");
        queueMap.put("L","Loans");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_report);

        TextView tvName,tvDate,tvTransaction,tvStartTime;
        EditText etFeedback;
        Button btnSubmit;

        tvName = findViewById(R.id.customer_report_tvCustName);
        tvDate = findViewById(R.id.customer_report_tvDate);
        tvTransaction = findViewById(R.id.customer_report_tvTransactionType);
        tvStartTime = findViewById(R.id.customer_report_tvstartTime);
        btnSubmit = findViewById(R.id.customer_report_btnSubmit);
        etFeedback = findViewById(R.id.input_et_feedback);

        etFeedback.setEnabled(false);
        btnSubmit.setEnabled(false);

        //Retrieve Bundle
        Intent intent = getIntent();
        customer cust = (customer) intent.getSerializableExtra("customerDetail");

        String token = cust.getToken();
        queueNo = cust.getQueueNo();

        Date date = new Date();

        SimpleDateFormat dateFormat_date,dateFormat_time;

        dateFormat_date = new SimpleDateFormat("dd-MM-YY");
        String formatDate = dateFormat_date.format(date);

        dateFormat_time = new SimpleDateFormat("hh:mm");
        String formatTime = dateFormat_time.format(date);

        String trans = queueMap.get(cust.getQueueType());

        tvName.setText(cust.getName());
        tvDate.setText(formatDate);
        tvTransaction.setText(trans);
        tvStartTime.setText(formatTime);

        //TODO Create Today Collection if not exist (WRONG PLACE) IN STAFF MODULE WHERE THEY END APPOINTMENT;
        DocumentReference docRef = db.collection("Report").document(formatDate);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("userPref", 0); // 0 - for private mode

        String counterLevel="1";
        String counterNo = pref.getString("counterNo","DEFAULT");


        String title = "Its your turn!";
        String message = "Please proceed to Level " + counterLevel + ", Counter "+counterNo;

        FcmNotificationSender notiSender = new FcmNotificationSender(token,title,message,this,this);

        notiSender.SendNotifications();
        startTimer();


        db.collection("Queue").document(cust.getQueueNo()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                customer cust1 = documentSnapshot.toObject(customer.class);
                if(cust1.getAcknowledge()){
                    cancelTimer();
                    setVibrate();
                    showNotification("Queue Acknowledged", queueNo+" has acknowledged the Queue");
                    btnSubmit.setEnabled(true);
                    etFeedback.setEnabled(true);
                } else if (!cust1.getAcknowledge()) {
                    cancelTimer();
                    setVibrate();
                    showNotification("Queue was not acknowledged", queueNo+" did not acknowledged the Queue");
                }
            }
        });

    }

    void startTimer() {
        cTimer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CreateNotificationChannel();
                }
                showNotification("Times up!","Customer did not acknowledge queue");
                setVibrate();
            }
        };
        cTimer.start();
    }


    //cancel timer
    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }


    void setVibrate(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {200};
        v.vibrate(pattern, -1);
    }

    private void showNotification(String title,String body){
        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this, CustomerReport.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"channel_id")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setAutoCancel(true);

        notificationManager.notify(1,builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateNotificationChannel(){
        NotificationChannel notificationChannel = new NotificationChannel("channel_id","Test Notification",NotificationManager.IMPORTANCE_HIGH);

        notificationChannel.setDescription("Its your queue number");

        NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
    }

}