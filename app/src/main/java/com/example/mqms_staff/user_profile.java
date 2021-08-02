package com.example.mqms_staff;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mqms_staff.Classes.UserClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.UUID;

public class user_profile extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private Uri imageUri;
    private ImageView imgView;
    private Button btnChngProfile;
    private TextView tvName,tvEmail,tvDept,tvCounter;
    private String TAG = "Firestore";
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        imgView = findViewById(R.id.user_img);
        btnChngProfile = findViewById(R.id.user_profile_btnChangeProfile);
        tvName = findViewById(R.id.user_profile_tvName);
        tvEmail = findViewById(R.id.user_profile_tvEmail);
        tvDept = findViewById(R.id.user_profile_tvDept);
        tvCounter = findViewById(R.id.user_profile_tvCounter);

        UserClass userC = (UserClass) getIntent().getSerializableExtra("userDetail");
        tvName.setText(userC.getName());
        tvEmail.setText(userC.getEmail());
        tvDept.setText(userC.getDepartment());
        tvCounter.setText(userC.getCounter());
        String imgSrc = userC.getImgSrc();
        if(!imgSrc.equals("") && imgSrc != null) {
            Picasso.get().load(imgSrc).into(imgView);
        } else {
            imgView.setImageResource(R.drawable.user_profile_icon);
        }

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getApplicationContext(),"Upload in progress",Toast.LENGTH_SHORT).show();
                }else{
                    choosePicture();
                }
            }
        });

        btnChngProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask !=null && mUploadTask.isInProgress()) {
                    Toast.makeText(getApplicationContext(),"Upload in progress",Toast.LENGTH_SHORT).show();
                }else {
                    choosePicture();
                }
            }
        });
    }

    private void choosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode== RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgView.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image....");
        pd.setCancelable(false);
        pd.show();

        StorageReference imgRef = storageRef.child(getUserID());

        mUploadTask = imgRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgRef.getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                db.collection("Users").document(getUserID()).update("imgSrc",uri.toString());
                            }
                        });


                pd.dismiss();
                Snackbar.make(findViewById(android.R.id.content),"Image Uploaded",Snackbar.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Snackbar.make(findViewById(android.R.id.content),"Failed to Upload",Snackbar.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                pd.setMessage("Progress: " + (int) progressPercent + "%");
            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

    private String getUserID(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("userPref", 0); // 0 - for private mode
        return pref.getString("userID","DEFAULT");
    } // end get token
}