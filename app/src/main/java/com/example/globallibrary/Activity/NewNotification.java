package com.example.globallibrary.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.globallibrary.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewNotification extends AppCompatActivity {
    EditText Title;
    EditText Discreption;
    ImageButton send;
    String BranchId;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notification);
        send = findViewById(R.id.send_new_notification);
        Title = findViewById(R.id.title_new_notification);
        Discreption = findViewById(R.id.body_new_notification);
        Intent intent = getIntent();
        BranchId = intent.getStringExtra("branchId");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                                    java.util.Date currentTime = Calendar.getInstance().getTime();
                                    String time  = currentTime.toString().trim();
                                    String day = time.substring(0,3);
                                    String date =  time.substring(8,10) +" "+ time.substring(4,7) +" " + time.substring(30,34) ;
                                    String CurrentT = time.substring(11,19);
                                    Log.d("tell tell", "onComplete: " + time);
                                    Map<String,Object> NewNotification = new HashMap<>();
                                    NewNotification.put("Title", Title.getText().toString());
                                    NewNotification.put("Discreption", Discreption.getText().toString());
                                    NewNotification.put("Day", day);
                                    NewNotification.put("Date", date);
                                    NewNotification.put("Time", CurrentT);
                                    String autoID = firebaseFirestore.collection("/Branches").document(BranchId).collection("Notifications").document().getId();
                                    Log.d("Checking", "onComplete: " + autoID);
                                    firebaseFirestore.collection("/Branches").document(BranchId).collection("Notifications").document(autoID).set(NewNotification);
                                    Intent intent = new Intent( NewNotification.this,GeneralActivity.class );
                                    intent.putExtra("user","branchAccess");
                                    intent.putExtra("branchId" , BranchId);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
            }
        });




    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this ,GeneralActivity.class );
        intent.putExtra("user","branchAccess");
        intent.putExtra("branchId" , BranchId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        super.onBackPressed();
    }

}