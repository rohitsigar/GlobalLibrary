package com.example.globallibrary.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Adpaters.FeeDetailStudentSideAdaptor;
import com.example.globallibrary.Models.FeeDetailsStudentSide;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FeeStudentFragment extends Fragment {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String BranchId;
    String StudentId;

    public RecyclerView recyclerView;
    public FeeDetailStudentSideAdaptor adapter;
    public RecyclerView.LayoutManager _layoutManager;

    ArrayList<FeeDetailsStudentSide> list;
    FeeDetailStudentSideAdaptor.RecyclerViewClickListner listner;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_fee_student, container, false);
        BranchId = getArguments().getString("BranchId");
        StudentId = getArguments().getString("StudentId");

        recyclerView = (RecyclerView) v.findViewById(R.id.student_fee_recyclerview);
        RecyclerView.LayoutManager _layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setFocusable(false);

        list = new ArrayList();

        firestore.collection("/Branches/" + BranchId + "/StudentDetails/" + StudentId + "/Fee").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            private static final String TAG = "Rohit";

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document1 : task.getResult())
                    {

                        String s = String.valueOf( Math.round(document1.getDouble("Date") ))+ "-" + String.valueOf( Math.round(document1.getDouble("Month")) )+ "-" +String.valueOf( Math.round(document1.getDouble("Year")) );
                        Log.d("TAG", "onComplete: "  + StudentId  + " " + document1.getId());
                       list.add(new FeeDetailsStudentSide(StudentId , document1.getDouble("Amount")  ,s , BranchId , document1.getId() , document1.getBoolean("Status") ));
                        adapter.notifyDataSetChanged();


                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(_layoutManager);
        setOnClickListner();

        adapter = new FeeDetailStudentSideAdaptor(list , listner);
        ///  set Adapter to RecyclerView
        recyclerView.setAdapter(adapter);


        return v;
    }
    private void setOnClickListner() {
        listner  = new FeeDetailStudentSideAdaptor.RecyclerViewClickListner() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(getActivity() , "yes :" + list.get(position).UniquePaymentId , Toast.LENGTH_SHORT).show();

            }
        };

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}