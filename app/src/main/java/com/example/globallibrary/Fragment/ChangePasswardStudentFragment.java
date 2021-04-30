package com.example.globallibrary.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePasswardStudentFragment extends Fragment {
    TextInputEditText CurrentPassward;
    TextInputEditText NewPassward;


    MaterialButton Change1;
    Button ForgetPassward;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();





    String StudentId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_passward_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CurrentPassward = view.findViewById(R.id.current_passward_student1);
        NewPassward = view.findViewById(R.id.new_passward_student);
        Change1 = view.findViewById(R.id.change1student);
        ForgetPassward = view.findViewById(R.id.forget_password_student1);

        StudentId = getArguments().getString("StudentId");


        Change1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docIdRef = firebaseFirestore.collection("/Students/" ).document(StudentId.trim());
                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if(document.getString("Passward")==CurrentPassward.getText().toString().trim())
                                {

                                    firebaseFirestore.collection("Students").document(StudentId.trim()).update("Passward" , NewPassward.getText().toString().trim());
                                    Toast.makeText(getActivity() , "Passward is Sucessfully Changed" , Toast.LENGTH_SHORT);
                                }
                                else
                                {
                                    CurrentPassward.setError("Wrong passward");
                                }


                            } else {

                                Log.d("TAG", "onComplete: not possible" );

                            }
                        } else {

                        }
                    }
                });
            }
        });
        ForgetPassward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //yet to impliment
            }
        });
    }
}