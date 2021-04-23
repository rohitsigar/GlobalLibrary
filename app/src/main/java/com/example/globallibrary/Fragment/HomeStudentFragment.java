package com.example.globallibrary.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Activity.StudentAttandance;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
// pending
//1. store phone number locally in phone and keep student user loged in for now user have to log in every time

public class HomeStudentFragment extends Fragment {

    RecyclerView recyclerView;
    String StudentId;
    String branchId;
    TextView setQuote;
    String quoteUpdate  = "Hello";
    CardView showAttandance;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_student_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        StudentId = getArguments().getString("StudentId");
        branchId = getArguments().getString("BranchId");

        Log.d("phone", "onViewCreated: " + StudentId); //getting unique phone number from sigh in page.
        setQuote = view.findViewById(R.id.studentQuote);
        showAttandance = view.findViewById(R.id.show_student_attandance);
        showAttandance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , StudentAttandance.class);
                intent.putExtra("StudentId" , StudentId);
                intent.putExtra("BranchId" , branchId);
                startActivity(intent);
            }
        });
        DocumentReference docIdRef = firestore.collection("/Branches/" ).document(branchId.trim());
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                        setQuote.setText(document.getString("Quote"));

                    } else {

                        Log.d("TAG", "onComplete: not possible" );

                    }
                } else {

                }
            }
        });




    }
}