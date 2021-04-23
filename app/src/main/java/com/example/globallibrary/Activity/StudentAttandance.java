package com.example.globallibrary.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.globallibrary.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class StudentAttandance extends AppCompatActivity {
   CustomCalendar customCalendar;
    String CurrentMonth;
    String s;
    String StudentId;
    String BranchId;
    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attandance);
       customCalendar = findViewById(R.id.custom_calendar);
        s = (String) customCalendar.getMonthYearTextView().getText().toString();
        firebaseFirestore = FirebaseFirestore.getInstance();

        java.util.Date currentTime = Calendar.getInstance().getTime();
        String time  = currentTime.toString().trim();
 CurrentMonth  = time.substring(4,7) +" " + time.substring(30,34);
String CurrentDate = time.substring(8,10) +" "+ time.substring(4,7) +" " + time.substring(30,34);
        Log.d("TAG", "onCreate: checking" + s + " " + CurrentMonth);
        Intent intent = getIntent();
        StudentId = intent.getExtras().getString("StudentId");
        BranchId  = intent.getExtras().getString("BranchId");



        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        String MonthOnly  = formattedDate.substring(3,9);






        HashMap<Object, Property> mapDescToProp = new HashMap<>();

        Property propDefault = new Property();
        propDefault.layoutResource = R.layout.default_view_calender_date;
        propDefault.dateTextViewResource = R.id.text_view;
        mapDescToProp.put("default", propDefault);

        Property propPresent= new Property();
        propPresent.layoutResource = R.layout.present_view_calender_date;
        //You can leave the text view field blank. Custom calendar won't try to set a date on such views
        propPresent.dateTextViewResource  = R.id.text_view;
        propPresent.enable = false;
        mapDescToProp.put("Present", propPresent);

        Property propAbsent = new Property();
        propAbsent.layoutResource = R.layout.absent_view_calender_date;
        propAbsent.dateTextViewResource = R.id.text_view;
        mapDescToProp.put("Absent", propAbsent);

        customCalendar.setMapDescToProp(mapDescToProp);


        HashMap<Integer, Object> dateHastMap = new HashMap<Integer, Object>();
        Calendar calendar = Calendar.getInstance();
        dateHastMap.put(1 ,"Present");
        dateHastMap.put(2, "Present");
        dateHastMap.put(3 ,"Absent");
        customCalendar.setDate(calendar,dateHastMap);
        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                String SDate = selectedDate.get(Calendar.DAY_OF_MONTH)
                         + "-" + (selectedDate.get(Calendar.MONTH)+1)
                         + "-" + selectedDate.get(Calendar.YEAR);
                Toast.makeText(StudentAttandance.this , SDate , Toast.LENGTH_SHORT).show();
            }
        });
//        firebaseFirestore.collection("Students/").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful())
//                {
//                    for(QueryDocumentSnapshot document : task.getResult())
//                    {
//                        if(document.getString("ContactNumber").equals(PhoneNo))
//                        {
//                            firebaseFirestore.collection("Branches/").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                    if(task.isSuccessful())
//                                    {
//                                        for(QueryDocumentSnapshot document1 : task.getResult())
//                                        {
//                                            if(document1.getString("BranchName").equals(document.getString("BranchName")))
//                                            {
//                                                DocumentReference docIdRef = firebaseFirestore.collection("Branches/" + document1.getId() + "/StudentsDetails/" + document.getId() + "/Attandance/").document(MonthOnly);
//                                                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                                        if (task.isSuccessful()) {
//                                                            DocumentSnapshot document = task.getResult();
//                                                            if (document.exists()) {
//
//                                                                s = (String) customCalendar.getMonthYearTextView().getText().toString();
//                                                                if(s.equals(CurrentMonth))
//                                                                {
//                                                                    for(int i =0; i<)
//                                                                }
//
//
//
//
//
//
//
//
//
//                                                                //existes
//
//                                                            } else {
//                                                                //not existss
//
//                                                            }
//                                                        } else {
//
//                                                        }
//                                                    }
//                                                });
//                                            }
//                                        }
//                                    }
//
//                                }
//                            });
//                        }
//                    }
//                }
//            }
//        });















    }


}