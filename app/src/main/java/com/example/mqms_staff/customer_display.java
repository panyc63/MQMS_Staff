package com.example.mqms_staff;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mqms_staff.Adapter.customerAdapter;
import com.example.mqms_staff.Classes.UserClass;
import com.example.mqms_staff.Classes.customer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class customer_display extends AppCompatActivity implements customerAdapter.OnItemListener {

    private final String TAG = "Firestore";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<customer> customerList;
    private customerAdapter adapter;
    private UserClass userClass;
    private boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_display);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        userClass = (UserClass) intent.getSerializableExtra("userDetail");

        HashMap<String, String> queueMap = new HashMap<>();
        queueMap.put("General Enquiries","GE");
        queueMap.put("Selective En bloc Redevelopment Scheme(SERS)","SE");
        queueMap.put("Sales Agreement","SA");
        queueMap.put("Loans","L");

        Date date = new Date();
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formatDate = dateFormat.format(date);

        String dept = queueMap.get(userClass.getDepartment());
        initRecyclerView();
        // TODO Realtime Update on the queue
        db.collection(formatDate).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                customerList.clear();
                List<DocumentSnapshot> list =  queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){

                    customer obj = d.toObject(customer.class);
                    obj.setQueueNo(d.getId());
                    boolean checker = obj.getQueueNo().contains(dept);

                    if(checker){
                        customerList.add(obj);
                    }
                }
                adapter.notifyDataSetChanged();

            }
        });



    }//end of onCreate

    private void initRecyclerView(){
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.recyclerCustomer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        customerList = new ArrayList<>();
        adapter = new customerAdapter(customerList,this);
        recyclerView.setAdapter(adapter);
    }

    private void updateCounter(String dept,String counterNo,String queueNo){
        db.collection(dept).document(counterNo).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    db.collection(dept).document(counterNo).update("CurrentServing",queueNo);
                } else if(!documentSnapshot.exists()){
                    Log.d(TAG,"Counter does not exist");
                }
            }
        });
    }


    @Override
    public void onItemClick(int position) {
        Toast.makeText(getApplicationContext(),"Item: " + String.valueOf(position) + " is clicked" , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),CustomerReport.class);
        customer cust = customerList.get(position);
        intent.putExtra("customerDetail",(Serializable) cust);
        String counter = "Counter " + userClass.getCounter();
        updateCounter(userClass.getDepartment(),counter,cust.getQueueNo());
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();


        //if user does not click back again in 2 second reset
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }


}