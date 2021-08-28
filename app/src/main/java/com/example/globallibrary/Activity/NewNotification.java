package com.example.globallibrary.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.globallibrary.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewNotification extends AppCompatActivity {
    EditText Title;
    EditText Discreption;
    MaterialButton send;
    String BranchId;
    ImageButton BackPress;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.main_color));
        }
        setContentView(R.layout.activity_new_notification);
        send = findViewById(R.id.send_new_notification);
        Title = findViewById(R.id.title_new_notification);
        Discreption = findViewById(R.id.body_new_notification);
        BackPress = findViewById(R.id.return_to_notification);
        BackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();

            }
        });
        Intent intent = getIntent();
        BranchId = intent.getStringExtra("branchId");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Title.getText().toString().trim().isEmpty())
                {
                    Title.setError("This Filed is Empty");
                }
                else if(Discreption.getText().toString().trim().isEmpty())
                {
                    Discreption.setError("This Field is Empty");
                }
                else
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
                    NewNotification.put("Sortthis" , currentTime);
                    String autoID = firebaseFirestore.collection("/Branches").document(BranchId).collection("Notifications").document().getId();
                    Log.d("Checking", "onComplete: " + autoID);
                    firebaseFirestore.collection("/Branches").document(BranchId).collection("Notifications").document(autoID).set(NewNotification);
                    Intent intent = new Intent( NewNotification.this,GeneralActivity.class );
                    intent.putExtra("user","branchAccess");
                    intent.putExtra("branchId" , BranchId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }




            }
        });




    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}