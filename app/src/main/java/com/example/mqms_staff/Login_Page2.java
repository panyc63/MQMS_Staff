package com.example.mqms_staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mqms_staff.Classes.UserClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Login_Page2 extends AppCompatActivity {
    private TextView _welc;
    private EditText _password;
    private Button btnLogin;
    private ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__page2);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        UserClass userClass = (UserClass) intent.getSerializableExtra("userDetail");
        imgView = findViewById(R.id.login_img);

        String imgSrc = userClass.getImgSrc();
        Picasso.get().load(imgSrc).into(imgView);

        _welc = findViewById(R.id.tv_Welc_Login);
        _welc.setText("Welcome! " + userClass.getName());


        _password = (EditText) findViewById(R.id.et_Password_Login);
        btnLogin = (Button) findViewById(R.id.btn_login2);
        String password = userClass.getPassword();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = _password.getText().toString();

                BCrypt.Result result = BCrypt.verifyer().verify(pass.toCharArray(), password);
                if (result.verified) {
                    Intent intent1 = new Intent(getApplicationContext(),home_page.class);
                    intent1.putExtra("userDetail",(Serializable) userClass);

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("userPref", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("counterNo",userClass.getCounter());
                    editor.putString("dept", userClass.getDepartment());
                    editor.apply();

                    startActivity(intent1);
                    finish();

                } else {
                    _password.setError("Incorrect Password");
                }
            }
        });
    }
}