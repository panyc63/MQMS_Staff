package com.example.mqms_staff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mqms_staff.Classes.UserClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.firestore.model.DocumentCollections;

import org.w3c.dom.Document;

import java.io.Serializable;

public class Login_Page extends AppCompatActivity {
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__page);

        EditText _id;
        Button login;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String TAG = "Firestore";

        login = findViewById(R.id.btn_login);
        _id = findViewById(R.id.et_StaffID_login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id, password;
                Log.d(TAG,getToken());

                id = _id.getText().toString();

                if (id.equals("")) {
                    _id.setError("Please enter ID");
                    return;
                }
                
                DocumentReference userRef = db.collection("Users").document(id);

                userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot dSnap = task.getResult();
                            if(dSnap.exists()) {
                                UserClass uClass = dSnap.toObject(UserClass.class);
                                Log.d(TAG,uClass.getCounter());
                                Intent intent = new Intent(getApplicationContext(), Login_Page2.class);
                                intent.putExtra("userDetail",(Serializable) uClass);
                                startActivity(intent);

                            } else {
                                _id.setError("User does not exist");
                                return;
                            }
                        }
                    }
                });



            } // onclick event
        }); //login button onclick

    } // oncreate

    public String getToken(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("userPref", 0); // 0 - for private mode
        String token = pref.getString("token","DEFAULT");
        return token;
    } // end get token
}
