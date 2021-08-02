package com.example.mqms_staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mqms_staff.Classes.UserClass;

import java.io.Serializable;

public class home_page extends AppCompatActivity {
    private CardView btnCustomer,btnProfile,btnGenerateReport;
    private TextView tvWelc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        tvWelc = findViewById(R.id.tv_Welc);

        Intent intent = getIntent();
        UserClass userClass = (UserClass) intent.getSerializableExtra("userDetail");
        tvWelc.setText("Welcome back, "+userClass.getName());
        btnCustomer = (CardView) findViewById(R.id.cardview_Customer);
        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),customer_display.class);
                intent.putExtra("userDetail", (Serializable)userClass);
                startActivity(intent);
            }
        });

        btnProfile = (CardView) findViewById(R.id.cardview_myInfo);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),user_profile.class);
                intent.putExtra("userDetail", (Serializable)userClass);
                startActivity(intent);
            }
        });

        btnGenerateReport = findViewById(R.id.cardView_generateReport);

        btnGenerateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),generateReport.class);
                startActivity(intent);

            }
        });

    }

}