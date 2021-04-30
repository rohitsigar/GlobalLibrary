package com.example.globallibrary.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Adpaters.StudentReportAdaptor;
import com.example.globallibrary.Models.ReportQuizStudentItem;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class QuizReportStudent extends AppCompatActivity {

    String StudentId;
    String BranchId;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    public RecyclerView recyclerView;
    public RecyclerView.Adapter _mAdapter;
    public RecyclerView.LayoutManager _layoutManager;
    TextView textView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_report_student);

        Intent intent = getIntent();
        StudentId = intent.getStringExtra("StudentId");
        BranchId = intent.getStringExtra("BranchId");
        textView = findViewById(R.id.text_view_2);
        recyclerView = (RecyclerView) findViewById(R.id.report_quiz_student);
        RecyclerView.LayoutManager _layoutManager = new LinearLayoutManager(this);

        recyclerView.setFocusable(false);
        ArrayList<ReportQuizStudentItem> list = new ArrayList();

        firestore.collection("/Branches/" + BranchId + "/StudentDetails/" + StudentId + "/QuizReport/").orderBy("Sortthis", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            private static final String TAG = "Rohit";


            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if(!task.getResult().isEmpty())
                    {
                        textView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        textView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                    for (QueryDocumentSnapshot document : task.getResult()) {


                        list.add(new ReportQuizStudentItem(document.getString("Performance")  ,document.getString("Catigory") ,  document.getString("Difficulty") , document.getString("Day") , document.getString("Date") , document.getString("Time")));


                        _mAdapter.notifyDataSetChanged();
                        ///  add items to the adapter
                    }


                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(_layoutManager);
        ///  add items to the adapter
        _mAdapter = new StudentReportAdaptor(list);
        ///  set Adapter to RecyclerView
        recyclerView.setAdapter(_mAdapter);



    }
}