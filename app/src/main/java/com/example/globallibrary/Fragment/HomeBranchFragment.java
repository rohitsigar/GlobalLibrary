package com.example.globallibrary.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.globallibrary.Activity.StudentPallet;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class HomeBranchFragment extends Fragment {

TextInputEditText Quote;
TextView setQuote;
    CardView StudentPallet1;
String branchId;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_branch_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setQuote = view.findViewById(R.id.branchSetQuote);
        Quote = view.findViewById(R.id.branchQuote);
        branchId = getArguments().getString("branchId");
        StudentPallet1 = view.findViewById(R.id.student_pallet);
        StudentPallet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), StudentPallet.class);
                intent.putExtra("branchId", branchId);
                startActivity(intent);
            }
        });
        setQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference docIdRef = firebaseFirestore.collection("Branches/" ).document(branchId);
                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {


                                firebaseFirestore.collection("Branches").document(document.getId()).update("Quote" ,Quote.getText().toString() );

                            } else {

                            }
                        } else {

                        }
                    }
                });

            }
        });

    }
}