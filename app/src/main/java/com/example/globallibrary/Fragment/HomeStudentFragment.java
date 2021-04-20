package com.example.globallibrary.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
// pending
//1. store phone number locally in phone and keep student user loged in for now user have to log in every time

public class HomeStudentFragment extends Fragment {

    RecyclerView recyclerView;
    String phoneNo;
    String branchName;
    TextView setQuote;
    String quoteUpdate  = "Hello";

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

        recyclerView = view.findViewById(R.id.branchSlider1);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        phoneNo = getArguments().getString("phoneNo");
        Log.d("phone", "onViewCreated: " + phoneNo); //getting unique phone number from sigh in page.
        setQuote = view.findViewById(R.id.studentQuote);
        firestore.collection("/Students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            private static final String TAG = "Rohit";

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) { //by triversing student collection i am getting branch name using phone number
                        if(document.getString("ContactNumber").equals(phoneNo)) // of student
                        {
                            branchName = document.getString("BranchName");
                            firestore.collection("Branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                private static final String TAG = "Rohit"; // here as a a get branch name above we will use thi  brsnch name of get quote form branch collection

                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if (document.getString("BranchName").equals(branchName)) {
                                                quoteUpdate = document.getString("Quote");
                                                Log.d("Quote", "onComplete: " + quoteUpdate);
                                                setQuote.setText(quoteUpdate);
                                            }
                                        }

                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                            Log.d(TAG, "onComplete123: "+ branchName);
                        }

                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });




    }
}