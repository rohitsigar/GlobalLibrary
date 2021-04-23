package com.example.globallibrary.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StudentAttandance extends AppCompatActivity implements OnNavigationButtonClickedListener {
   CustomCalendar customCalendar;
    String CurrentMonth;
    String s;
    String StudentId;
    String BranchId;
    FirebaseFirestore firebaseFirestore;
    Map<String , String> Map1 = new HashMap<String, String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attandance);
       customCalendar = findViewById(R.id.custom_calendar);
        s = (String) customCalendar.getMonthYearTextView().getText().toString();
        firebaseFirestore = FirebaseFirestore.getInstance();


        Map1.put("Jan" , "01");
        Map1.put("Feb" , "02");
        Map1.put("Mar" , "03");
        Map1.put("Apr" , "04");
        Map1.put("May" , "05");
        Map1.put("Jun" , "06");
        Map1.put("Jul" , "07");
        Map1.put("Aug" , "08");
        Map1.put("Sep" , "09");
        Map1.put("Oct" , "10");
        Map1.put("Nov" , "11");
        Map1.put("Dec" , "12");
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


        HashMap<Integer, Object> mapDateToDesc = new HashMap<Integer, Object>();
        Calendar calendar = Calendar.getInstance();




        String doc = Map1.get(customCalendar.getMonthYearTextView().getText().toString().substring(0,3)) + "-" + customCalendar.getMonthYearTextView().getText().toString().substring(4,8);
        Log.d("TAG", "onCreate: checking" +doc);
        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                String SDate = selectedDate.get(Calendar.DAY_OF_MONTH)
                         + "-" + (selectedDate.get(Calendar.MONTH)+1)
                         + "-" + selectedDate.get(Calendar.YEAR);
                Toast.makeText(StudentAttandance.this , SDate , Toast.LENGTH_SHORT).show();
            }
        });
        Log.d("TAG", "onCreate: faad " +"/Branches/" + BranchId.trim() + "/StudentDetails/" + StudentId.trim() + "/Attandance/" + doc );

        DocumentReference docIdRef = firebaseFirestore.collection("/Branches/" + BranchId.trim() + "/StudentDetails/" + StudentId.trim() + "/Attandance/" ).document(doc.trim());
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        String formattedDate = df.format(c);
                        int CurrentDate  = Integer.parseInt(formattedDate.substring(0,2));
                        for(int i=1;i<CurrentDate + 1;i++)
                        {
                            String Day = Integer.toString(i);
                            if(document.getBoolean(Day)!=null)
                            {
                                mapDateToDesc.put(i ,"Present");
                            }
                            else
                            {
                                mapDateToDesc.put(i ,"Absent");
                            }
                        }
                        customCalendar.setDate(calendar, mapDateToDesc);






                    } else {
                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        String formattedDate = df.format(c);
                        int CurrentDate  = Integer.parseInt(formattedDate.substring(0,2));
                        for(int i=1;i<CurrentDate + 1;i++)
                        {
                            mapDateToDesc.put(i ,"Absent");
                        }
                        customCalendar.setDate(calendar, mapDateToDesc);


                        Log.d("TAG", "onComplete: not possible" );


                    }
                } else {

                }
            }
        });

        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);
        customCalendar.setDate(calendar, mapDateToDesc);
        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                Snackbar.make(customCalendar, selectedDate.get(Calendar.DAY_OF_MONTH) + " selected", Snackbar.LENGTH_LONG).show();
            }
        });













    }
    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
        Map<Integer, Object>[] arr = new Map[2];
        int month = newMonth.get(Calendar.MONTH)+1;
        int year = newMonth.get(Calendar.YEAR);
        String doc = "";
        if(month < 10)
        {
            doc  = "0" + Integer.toString(month) +"-" + Integer.toString(year);
        }
        else
        {
            doc  = Integer.toString(month) +"-" + Integer.toString(year);
        }
        Log.d("TAG", "onNavigationButtonClicked: hey baby " + doc);
        arr[0] = new HashMap<>();
        DocumentReference docIdRef = firebaseFirestore.collection("/Branches/" + BranchId.trim() + "/StudentDetails/" + StudentId.trim() + "/Attandance/" ).document(doc.trim());
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        YearMonth yearMonthObject = YearMonth.of(year, month);
                        int daysInMonth = yearMonthObject.lengthOfMonth();

                        for(int i=1;i<daysInMonth + 1;i++)
                        {
                            String Day = Integer.toString(i);
                            if(document.getBoolean(Day.toString())!=null)
                            {
                                arr[0].put(i ,"Present");
                            }
                            else
                            {
                                arr[0].put(i ,"Absent");
                            }
                        }
                        arr[1] = null;
                        customCalendar.setDate(newMonth, arr[0]);
                    } else {
                        YearMonth yearMonthObject = YearMonth.of(year, month);
                        int daysInMonth = yearMonthObject.lengthOfMonth();
                        Log.d("TAG", "onComplete: check high level " + daysInMonth);
                        for(int i=1;i<daysInMonth + 1;i++)
                        {
                            Log.d("TAG", "onComplete: counting " + i);

                                arr[0].put(i ,"Absent");
                        }
                        arr[1] = null;
                        customCalendar.setDate(newMonth, arr[0]);
//                        customCalendar.setDate(calendar,arr[0]);
                        Log.d("TAG", "onComplete: not possible" );
                    }
                } else {

                }
            }
        });


//        switch(newMonth.get(Calendar.MONTH)) {
//            case Calendar.AUGUST:
//                arr[0] = new HashMap<>(); //This is the map linking a date to its description
//
//                arr[0].put(3, "Present");
//                arr[0].put(6, "Absent");
//                arr[0].put(21, "Present");
//                arr[0].put(24, "Present");
//                arr[1] = null; //Optional: This is the map linking a date to its tag.
//                break;
//            case Calendar.JUNE:
//                arr[0] = new HashMap<>();
//                arr[0].put(5, "Present");
//                arr[0].put(10, "Absent");
//                arr[0].put(19, "Present");
//                arr[1] = null;
//                break;
//        }
        return arr;
    }

}