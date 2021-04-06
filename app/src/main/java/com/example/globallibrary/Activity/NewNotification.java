package com.example.globallibrary.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewNotification extends AppCompatActivity {
    EditText Title;
    EditText Discreption;
    ImageButton send;
    String BranchName;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notification);
        send = findViewById(R.id.send_new_notification);
        Title = findViewById(R.id.title_new_notification);
        Discreption = findViewById(R.id.body_new_notification);
        Intent intent = getIntent();
        BranchName = intent.getStringExtra("branchName");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("/Branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "Rohit";

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("BranchName").equals(BranchName))
                                {
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
                                    String autoID = firebaseFirestore.collection("/Branches").document(document.getId()).collection("Notifications").document().getId();
                                    Log.d("Checking", "onComplete: " + autoID);
                                    firebaseFirestore.collection("/Branches").document(document.getId()).collection("Notifications").document(autoID).set(NewNotification);
                                    Intent intent = new Intent( NewNotification.this,GeneralActivity.class );
                                    intent.putExtra("user","branchAccess");
                                    intent.putExtra("branchName" , BranchName);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);


                                }
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

            }
        });


    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this ,GeneralActivity.class );
        intent.putExtra("user","branchAccess");
        intent.putExtra("branchName" , BranchName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        super.onBackPressed();
    }

}