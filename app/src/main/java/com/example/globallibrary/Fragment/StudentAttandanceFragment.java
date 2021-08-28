package com.example.globallibrary.Fragment;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
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

import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;


public class StudentAttandanceFragment extends Fragment implements OnNavigationButtonClickedListener {


    com.example.globallibrary.Activity.CustomCalendar customCalendar;
    String CurrentMonth;
    String s;
    String StudentId;
    String BranchId;
    FirebaseFirestore firebaseFirestore;
    Map<String , String> Map1 = new HashMap<String, String>();

    MaterialButton MarkAttandance;
    LinearLayout Marked;
    LinearLayout NotMaked;
    AlertDialog alertDialog;
    LinearLayout mainFrame;
    LinearLayout QrFrame;

    private ScannerLiveView camera;

    Date c;
    SimpleDateFormat df;
    String formattedDate;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_attandance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customCalendar = view.findViewById(R.id.custom_calendar);
        s = (String) customCalendar.getMonthYearTextView().getText().toString();
        firebaseFirestore = FirebaseFirestore.getInstance();

        customCalendar.setDividerDrawable(ContextCompat.getDrawable(getContext() , R.drawable.newbar_background));
        camera = view.findViewById(R.id.camview);
        MarkAttandance = view.findViewById(R.id.mark_attandance1);
        mainFrame = view.findViewById(R.id.main_layout);
        QrFrame = view.findViewById(R.id.scaner_layout);


        MarkAttandance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 c = Calendar.getInstance().getTime();
              df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                 formattedDate = df.format(c);
                String s1 = c.toString();
//                int hour = Integer.getInteger(s1.substring(11,13));
//                int minute = Integer.getInteger(s1.substring(14,16));
                Log.d("TAG", "onClick: checking" + s1.substring(11,13) + " "  + s1.substring(14,16) );

                String Hour = s1.substring(11,13);
                String Minute = s1.substring(14,16);
                Log.d("TAG", "onClick: checking " + Hour  + " " + Minute);
                int hour = Integer.parseInt(Hour);
                int minute = Integer.parseInt(Minute);
                DocumentReference docIdRef1 = firebaseFirestore.collection("/Branches/" ).document(BranchId.trim());
                docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                int OpenHour = document.getDouble("OpenHour").intValue();
                                int CloseHour = document.getDouble("CloseHour").intValue();
                                int OpenMinute = document.getDouble("OpenMinute").intValue();
                                int CloseMinute = document.getDouble("CloseMinute").intValue();

                                if(OpenHour > CloseHour || (OpenHour==CloseHour && OpenMinute > CloseMinute))
                                {
                                    int temp = OpenHour;
                                    OpenHour = CloseHour;
                                    CloseHour = temp;

                                    temp = OpenMinute;
                                    OpenMinute = CloseMinute;
                                    CloseMinute = temp;
                                    if(!(((hour > OpenHour) || (hour==OpenHour && minute >=OpenMinute)) && (hour < CloseHour || (hour== CloseHour && minute <=CloseMinute))))
                                    {
                                        ScanTheCode();
                                    }
                                    else
                                    {
                                        mainFrame.setVisibility(View.VISIBLE);
                                        QrFrame.setVisibility(View.GONE);
                                        ViewGroup viewGroup = getView().findViewById(android.R.id.content);
                                        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dilog_mark_attandance, viewGroup, false);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setView(dialogView);
                                        alertDialog = builder.create();
                                        alertDialog.show();
                                        LinearLayout AlreadyMarked = alertDialog.findViewById(R.id.not_on_time);
                                        AlreadyMarked.setVisibility(View.VISIBLE);
                                    }

                                }
                                else
                                {
                                    if(((hour > OpenHour) || (hour==OpenHour && minute >=OpenMinute)) && (hour < CloseHour || (hour== CloseHour && minute <=CloseMinute)))
                                    {


ScanTheCode();

                                    }
                                    else
                                    {

                                        mainFrame.setVisibility(View.VISIBLE);
                                        QrFrame.setVisibility(View.GONE);
                                        ViewGroup viewGroup = getView().findViewById(android.R.id.content);
                                        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dilog_mark_attandance, viewGroup, false);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setView(dialogView);
                                        alertDialog = builder.create();
                                        alertDialog.show();
                                        LinearLayout AlreadyMarked = alertDialog.findViewById(R.id.not_on_time);
                                        AlreadyMarked.setVisibility(View.VISIBLE);
                                    }

                                }






                            } else {


                            }
                        } else {

                        }
                    }
                });

            }
        });
        camera.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
                // method is called when scanner is started
                Toast.makeText(getActivity(), "Scanner Started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
                mainFrame.setVisibility(View.VISIBLE);
                QrFrame.setVisibility(View.GONE);
                // method is called when scanner is stoped.
                Toast.makeText(getActivity(), "Scanner Stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerError(Throwable err) {
                mainFrame.setVisibility(View.VISIBLE);
                QrFrame.setVisibility(View.GONE);
                // method is called when scanner gives some error.
                Toast.makeText(getActivity(), "Scanner Error: " + err.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeScanned(String data) {

                camera.stopScanner();
                // method is called when camera scans the
                // qr code and the data from qr code is
                // stored in data in string format.

                mainFrame.setVisibility(View.VISIBLE);
                QrFrame.setVisibility(View.GONE);
                ViewGroup viewGroup = getView().findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dilog_mark_attandance, viewGroup, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(dialogView);
                alertDialog = builder.create();
                alertDialog.show();
                Marked = alertDialog.findViewById(R.id.marked);
                NotMaked = alertDialog.findViewById(R.id.not_marked);


                DocumentReference docIdRef1 = firebaseFirestore.collection("/Branches/" ).document(BranchId.trim());
                docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                if(data.equals(document.getString("UniqueQrCode")))
                                {
                                    Marked.setVisibility(View.VISIBLE);
                                    NotMaked.setVisibility(View.GONE);
                                    Date c = Calendar.getInstance().getTime();
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    String formattedDate = df.format(c);
                                    String studentDoc = formattedDate.substring(3, 10);
                                    String doc = formattedDate.substring(0, 2);
                                    DocumentReference docIdRef = firebaseFirestore.collection("/Branches/" + BranchId.trim() + "/Attandance/").document(formattedDate.trim());
                                    docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    firebaseFirestore.collection("Branches").document(BranchId).collection("Attandance").document(formattedDate).update(StudentId, true);


                                                } else {
                                                    Map<String, Object> allDetails = new HashMap<>();
                                                    allDetails.put(StudentId, true);
                                                    firebaseFirestore.collection("Branches").document(BranchId).collection("Attandance").document(formattedDate).set(allDetails);
                                                }
                                            } else {

                                            }
                                        }
                                    });
                                    DocumentReference docIdRef1 = firebaseFirestore.collection("/Branches/" + BranchId.trim() + "/StudentDetails/"  + StudentId.trim() + "/Attandance/").document(studentDoc.trim());
                                    docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    firebaseFirestore.collection("Branches").document(BranchId).collection("StudentDetails").document(StudentId).collection("Attandance").document(studentDoc.trim()).update(doc , true);
                                                } else {
                                                    Map<String,Object> allDetails = new HashMap<>();
                                                    allDetails.put(doc, true);
                                                    firebaseFirestore.collection("Branches").document(BranchId).collection("StudentDetails").document(StudentId).collection("Attandance").document(studentDoc).set(allDetails);
                                                }
                                            } else {

                                            }
                                        }
                                    });

                                    getActivity().onBackPressed();



                                }
                                else
                                {
                                    Marked.setVisibility(View.GONE);
                                    NotMaked.setVisibility(View.VISIBLE);
                                }


                            } else {






                            }
                        } else {

                        }
                    }
                });
            }
        });

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

        StudentId = getArguments().getString("StudentId");
        BranchId  = getArguments().getString("BranchId");
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

//        Property propDisabled= new Property();
//        propDefault.layoutResource = R.layout.default_view_calender_date;
//        propDefault.dateTextViewResource = R.id.text_view;
//        mapDescToProp.put("disabled121", propDisabled);

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
        String doc = Map1.get(customCalendar.getMonthYearTextView().getText().toString().substring(0,3)) + "-" + customCalendar.getMonthYearTextView().getText().toString().substring(7,11);
        Log.d("TAG", "onCreate: checking" +doc);
        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                String SDate = selectedDate.get(Calendar.DAY_OF_MONTH)
                        + "-" + (selectedDate.get(Calendar.MONTH)+1)
                        + "-" + selectedDate.get(Calendar.YEAR);
                Toast.makeText(getActivity() , SDate , Toast.LENGTH_SHORT).show();
            }
        });
        Log.d("TAG", "onCreate: faad " +"/Branches/" + BranchId.trim() + "/StudentDetails/" + StudentId.trim() + "/Attandance/" + doc + " " + customCalendar.getMonthYearTextView().getText().toString() );

        firebaseFirestore.collection("Branches").document(BranchId).collection("StudentDetails").document(StudentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document1 = task.getResult();
                    Calendar calendar  = Calendar.getInstance();
                    long month = calendar.get(Calendar.MONTH)+1;
                    long year = calendar.get(Calendar.YEAR);
                    if(document1.getLong("JoinYear").equals(year) && document1.getLong("JoinMonth").equals(month))
                    {
                        int JoinDate  = Math.toIntExact((long) document1.getLong("JoinDate"));


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
                                        for(int i=JoinDate;i<CurrentDate + 1;i++)
                                        {
                                            String Day;
                                            if(i < 10)
                                            {
                                                Day = "0" + Integer.toString(i);
                                            }
                                            else
                                            {
                                                Day = Integer.toString(i);
                                            }

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
                                        for(int i=JoinDate;i<CurrentDate + 1;i++)
                                        {
                                            mapDateToDesc.put(i ,"Absent");
                                        }
                                        customCalendar.setDate(calendar, mapDateToDesc);




                                    }
                                } else {

                                }
                            }
                        });
                    }
                    else
                    {
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
                                            String Day;
                                            if(i < 10)
                                            {
                                                Day = "0" + Integer.toString(i);
                                            }
                                            else
                                            {
                                                Day = Integer.toString(i);
                                            }

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
                    }
                }

            }
        });



        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS,this::onNavigationButtonClicked);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this::onNavigationButtonClicked);
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
        arr[0] = new HashMap<>();

        String finalDoc = doc;
        firebaseFirestore.collection("Branches").document(BranchId).collection("StudentDetails").document(StudentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document1 = task.getResult();
                    if(document1.exists())
                    {
                        YearMonth yearMonthObject = YearMonth.of(year, month);
                        int daysInMonth = yearMonthObject.lengthOfMonth();

                        Calendar calendar1 = Calendar.getInstance();

                        DocumentReference docIdRef = firebaseFirestore.collection("/Branches/" + BranchId.trim() + "/StudentDetails/" + StudentId.trim() + "/Attandance/" ).document(finalDoc.trim());
                        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        if (month == calendar1.get(Calendar.MONTH) + 1 && year == calendar1.get(Calendar.YEAR) && document1.getLong("JoinMonth") == month &&
                                                document1.getLong("JoinYear") == year) {
                                            int JoinDate = Math.toIntExact((long) document1.getLong("JoinDate"));
                                            int date = calendar1.get(Calendar.DATE);

                                            for (int i = JoinDate; i < date + 1; i++) {
                                                String Day;
                                                if (i < 10) {
                                                    Day = "0" + Integer.toString(i);
                                                } else {
                                                    Day = Integer.toString(i);
                                                }
                                                if (document.getBoolean(Day.toString()) != null) {
                                                    arr[0].put(i, "Present");
                                                } else {
                                                    arr[0].put(i, "Absent");
                                                }
                                            }


                                        } else if (month == calendar1.get(Calendar.MONTH) + 1 && year == calendar1.get(Calendar.YEAR)) {
                                            int date = calendar1.get(Calendar.DATE);

                                            for (int i = 1; i < date + 1; i++) {
                                                String Day;
                                                if (i < 10) {
                                                    Day = "0" + Integer.toString(i);
                                                } else {
                                                    Day = Integer.toString(i);
                                                }
                                                if (document.getBoolean(Day.toString()) != null) {
                                                    arr[0].put(i, "Present");
                                                } else {
                                                    arr[0].put(i, "Absent");
                                                }
                                            }
                                        } else if (document1.getLong("JoinMonth") == month &&
                                                document1.getLong("JoinYear") == year) {


                                            int JoinDate = Math.toIntExact((long) document1.getLong("JoinDate"));

                                            for (int i = JoinDate; i < daysInMonth + 1; i++) {
                                                String Day;
                                                if (i < 10) {
                                                    Day = "0" + Integer.toString(i);
                                                } else {
                                                    Day = Integer.toString(i);
                                                }
                                                if (document.getBoolean(Day.toString()) != null) {
                                                    arr[0].put(i, "Present");
                                                } else {
                                                    arr[0].put(i, "Absent");
                                                }
                                            }
                                        } else {

                                            for (int i = 1; i < daysInMonth + 1; i++) {

                                                String Day;
                                                if (i < 10) {
                                                    Day = "0" + Integer.toString(i);
                                                } else {
                                                    Day = Integer.toString(i);
                                                }
                                                if (document.getBoolean(Day.toString()) != null) {
                                                    arr[0].put(i, "Present");
                                                } else {
                                                    arr[0].put(i, "Absent");
                                                }
                                            }
                                        }

                                        arr[1] = null;
                                        customCalendar.setDate(newMonth, arr[0]);
                                    }
                                    else
                                    {
                                        if (month == calendar1.get(Calendar.MONTH) + 1 && year == calendar1.get(Calendar.YEAR) && document1.getLong("JoinMonth") == month &&
                                                document1.getLong("JoinYear") == year) {
                                            int JoinDate = Math.toIntExact((long) document1.getLong("JoinDate"));
                                            int date = calendar1.get(Calendar.DATE);

                                            for (int i = JoinDate; i < date + 1; i++) {


                                                arr[0].put(i, "Absent");

                                            }


                                        } else if (month == calendar1.get(Calendar.MONTH) + 1 && year == calendar1.get(Calendar.YEAR)) {
                                            int date = calendar1.get(Calendar.DATE);

                                            for (int i = 1; i < date + 1; i++) {

                                                arr[0].put(i, "Absent");

                                            }
                                        } else if (document1.getLong("JoinMonth") == month &&
                                                document1.getLong("JoinYear") == year) {


                                            int JoinDate = Math.toIntExact((long) document1.getLong("JoinDate"));

                                            for (int i = JoinDate; i < daysInMonth + 1; i++) {

                                                arr[0].put(i, "Absent");

                                            }
                                        } else if ((document1.getLong("JoinMonth") > month && document1.getLong("JoinYear") == year) || (document1.getLong("JoinYear") >  year)) {


                                        } else if ((calendar1.get(Calendar.MONTH)+1< month && calendar1.get(Calendar.YEAR) == year) || (calendar1.get(Calendar.YEAR) < year)) {


                                        } else {

                                            for (int i = 1; i < daysInMonth + 1; i++) {

                                                arr[0].put(i, "Absent");

                                            }
                                        }
                                        arr[1] = null;
                                        customCalendar.setDate(newMonth, arr[0]);
                                    }
                                } else {


                                }
                            }
                        });
                    }
                }
                else
                {

                }
            }
        });


        return arr;
    }

    public  void ScanTheCode()
    {
        Log.d("TAG", "onComplete: Hello" + "hahahahahahahaha");
        DocumentReference docIdRef = firebaseFirestore.collection("/Branches/" + BranchId.trim() + "/Attandance/").document(formattedDate.trim());

        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(document.contains(StudentId.trim()))
                        {
                            ViewGroup viewGroup = getView().findViewById(android.R.id.content);
                            View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dilog_mark_attandance, viewGroup, false);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setView(dialogView);
                            alertDialog = builder.create();
                            alertDialog.show();
                            LinearLayout AlreadyMarked = alertDialog.findViewById(R.id.already_marked);
                            AlreadyMarked.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            mainFrame.setVisibility(View.GONE);
                            QrFrame.setVisibility(View.VISIBLE);

                            ZXDecoder decoder = new ZXDecoder();
                            // 0.5 is the area where we have
                            // to place red marker for scanning.
                            decoder.setScanAreaPercent(0.8);
                            // below method will set secoder to camera.
                            camera.setDecoder(decoder);
                            camera.startScanner();

                        }


                    } else {
                        mainFrame.setVisibility(View.GONE);
                        QrFrame.setVisibility(View.VISIBLE);
                        ZXDecoder decoder = new ZXDecoder();
                        // 0.5 is the area where we have
                        // to place red marker for scanning.
                        decoder.setScanAreaPercent(0.8);
                        // below method will set secoder to camera.
                        camera.setDecoder(decoder);
                        camera.startScanner();

                    }
                } else {

                }
            }
        });
    }



}