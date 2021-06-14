package com.example.globallibrary.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Adpaters.ShortStudentsDetailsAdaptor;
import com.example.globallibrary.Models.ShortStudentDetails;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StudentPallet extends AppCompatActivity {


    DatePickerDialog.OnDateSetListener setListener;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    StorageReference storageReference;
    String BranchId;
    ImageButton Back;
    ImageButton Attandance;
    ImageButton StudentPallet1;
    TextView HeadLine;
    LinearLayout DatePallet;
    TextView DateShow;
    ImageButton CanlenderDilog;
    ProgressBar progressBar;

    private Uri filePath;
    String URL  = "";
    public RecyclerView recyclerView;
    public ShortStudentsDetailsAdaptor adapter;
    public RecyclerView.LayoutManager _layoutManager;

    ArrayList<ShortStudentDetails> list;
    ShortStudentsDetailsAdaptor.RecyclerViewClickListner listner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_pallet);
        recyclerView = (RecyclerView) findViewById(R.id.Short_recyclerView);
        RecyclerView.LayoutManager _layoutManager = new LinearLayoutManager(this);
        recyclerView.setFocusable(false);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        progressBar = findViewById(R.id.student_pallet_progress_bar);

        Intent intent = getIntent();
        BranchId = intent.getStringExtra("branchId");
        Back = findViewById(R.id.return_to_home12);
        Attandance = findViewById(R.id.attandance_student_pallet);
        StudentPallet1 = findViewById(R.id.home_student_pallet);
        HeadLine  = findViewById(R.id.mainname);
        DatePallet = findViewById(R.id.DatePallet);
        DateShow = findViewById(R.id.showDateHere);
        CanlenderDilog = findViewById(R.id.changeDateUsingCalender);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        CanlenderDilog.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog  = new DatePickerDialog(
                        StudentPallet.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = "";
                        if(day<10)
                        {
                            date = date + "0" + day;
                        }
                        else
                        {
                            date = date + day;
                        }
                        if(month<10)
                        {
                            date = date + "-0" + month;
                        }
                        else
                        {
                            date = date + "-" + month;
                        }
                        date = date + "-" + year;
                        DateShow.setText(date);
                        String finalDate = date;
                                        DocumentReference docIdRef = firestore.collection("Branches/" + BranchId + "/Attandance/").document(finalDate);
                                        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {


                                                        for(ShortStudentDetails e : list)
                                                        {
                                                            Log.d("TAG", "onComplete:checking "   + e.getStudentId()  + document.getBoolean(e.StudentId.toString()));
                                                            if(document.getBoolean(e.StudentId.toString())!=null)
                                                            {
                                                                e.Color = "Green";
                                                                adapter.notifyDataSetChanged();
                                                            }
                                                            else
                                                            {
                                                                e.Color = "Red";
                                                                adapter.notifyDataSetChanged();
                                                            }
                                                        }

                                                        //existes

                                                    } else {
                                                        //not existss
                                                        for(ShortStudentDetails e : list)
                                                        {
                                                            e.Color = "Red";
                                                            adapter.notifyDataSetChanged();

                                                        }
                                                    }
                                                } else {

                                                }
                                            }
                                        });
                    }
                },year , month,day);
                datePickerDialog.show();
            }
        });




        Attandance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Attandance.setVisibility(View.GONE);
                StudentPallet1.setVisibility(View.VISIBLE);
                HeadLine.setVisibility(View.GONE);
                DatePallet.setVisibility(View.VISIBLE);

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);


                                DocumentReference docIdRef = firestore.collection("Branches/" + BranchId + "/Attandance/").document(formattedDate);
                                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {


                                                for(ShortStudentDetails e : list)
                                                {
                                                    Log.d("TAG", "onComplete:checking "   + e.getStudentId()  + document.getBoolean(e.StudentId.toString()));
                                                    if(document.getBoolean(e.StudentId.toString())!=null)
                                                    {
                                                        e.Color = "Green";
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                    else
                                                    {
                                                        e.Color = "Red";
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }

                                                //existes

                                            } else {
                                                //not existss
                                                for(ShortStudentDetails e : list)
                                                {
                                                    e.Color = "Red";
                                                    adapter.notifyDataSetChanged();

                                                }
                                            }
                                        } else {

                                        }
                                    }
                                });
                DateShow.setText(formattedDate);


            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        Log.d("TAG", "onCreate: checking 1 2 3 " + BranchId);

       list = new ArrayList();
                            firestore.collection("/Branches/" + BranchId + "/StudentDetails").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                private static final String TAG = "Rohit";
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document1 : task.getResult())
                                        {
                                            URL = "Student/" + document1.getId();
                                            Log.d(TAG, "onComplete: checking123" + URL);
                                           

                                            storageReference.child(URL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    Uri downloadUrl = uri;
                                                    String s = downloadUrl.toString();
                                                    Log.d(TAG, "onSuccess: hello");
                                                    list.add(new ShortStudentDetails(document1.getString("FullName") , document1.getString("Discreption") , s , document1.getId() , "NoColor"));
                                                    Log.d(TAG, "onSuccess: checkinghello" + list.size());

                                                    adapter.notifyDataSetChanged();


                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    list.add(new ShortStudentDetails(document1.getString("FullName") , document1.getString("Discreption") , "NoImage" , document1.getId() , "NoColor"));
                                                    Log.d(TAG, "onSuccess: checking" + list.size());

                                                }
                                            });
                                        }



                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });

        Log.d("TAG", "onCreate: hello guys");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(_layoutManager);
        setOnClickListner();

        adapter = new ShortStudentsDetailsAdaptor(list , listner);
        ///  set Adapter to RecyclerView
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();






    }

    private void setOnClickListner() {
        listner  = new ShortStudentsDetailsAdaptor.RecyclerViewClickListner() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(StudentPallet.this , StudentOverview.class);
                intent.putExtra("StudentId" ,list.get(position).getStudentId() );
                intent.putExtra("BranchId" , BranchId);
                startActivity(intent);
            }
        };

    }



}