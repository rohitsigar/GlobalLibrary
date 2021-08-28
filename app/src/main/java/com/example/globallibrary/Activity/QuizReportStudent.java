package com.example.globallibrary.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Adpaters.StudentReportAdaptor;
import com.example.globallibrary.Models.ReportQuizStudentItem;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

public class QuizReportStudent extends AppCompatActivity {

    String StudentId;
    String BranchId;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    public RecyclerView recyclerView;
    public RecyclerView.Adapter _mAdapter;
    public RecyclerView.LayoutManager _layoutManager;
    TextView textView;

    TextView StudentName , BranchName , TotalQuiz , Performance;
    CircleImageView StudentImage;


    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    StorageReference storageReference;
    GifImageView ProgressBar;



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
        setContentView(R.layout.activity_quiz_report_student);

        Intent intent = getIntent();
        StudentId = intent.getStringExtra("StudentId");
        BranchId = intent.getStringExtra("BranchId");
        textView = findViewById(R.id.text_view_2);
        recyclerView = (RecyclerView) findViewById(R.id.report_quiz_student);
        RecyclerView.LayoutManager _layoutManager = new LinearLayoutManager(this);
        ProgressBar = findViewById(R.id.progress_bar_10);
        ProgressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        StudentName = findViewById(R.id.student_name_quiz_report);
        BranchName  = findViewById(R.id.branch_name_quiz_report);
        TotalQuiz = findViewById(R.id.total_quiz_quiz_report);
        Performance = findViewById(R.id.performance_quiz_report);
        StudentImage  = findViewById(R.id.student_image_quiz_report);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        String URL = "Student/" + StudentId;
        storageReference.child(URL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Uri downloadUrl = uri;
                String s = downloadUrl.toString();
                Log.d("hello guys", "onSuccess: " + s);
                Picasso.get().load(s).into(StudentImage);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
//                StudentImage.setImageResource(R.drawable.student_logo);
                // Handle any errors
            }
        });


        firebaseFirestore.collection("/Branches/" + BranchId.trim() + "/StudentDetails/" ).document(StudentId.trim()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        StudentName.setText(document.getString("FullName"));
                        TotalQuiz.setText(String.valueOf(document.getLong("TotalQuiz")));
                        long value ;
                        long a = document.getLong("TotalQuestion");
                        long b = document.getLong("TotalRight");
                        if(a==0)
                        {
                            value = 0;
                        }
                        else
                        {
                            value = (b*100)/a;

                        }

                        Performance.setText("" + value + "%");


                    } else {

                        Log.d("TAG", "onComplete: not possible" );

                    }
                } else {

                }
            }
        });
        firebaseFirestore.collection("Branches").document(BranchId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        BranchName.setText(document.getString("BranchName"));


                    } else {

                        Log.d("TAG", "onComplete: not possible" );

                    }
                } else {

                }
            }
        });

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
                    ProgressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);


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