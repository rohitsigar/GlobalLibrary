package com.example.globallibrary.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.globallibrary.Activity.ForgetPasswardStudentActivity;
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


    private static final String KEY_ACCESS = "access";
    private static final String SHARED_PREF = "PREF";
    private static final String KEY_BRANCH_ID = "id";
    private static final String KEY_STUDENT_ID = "id_student";
    private static final String KEY_STUDENT_B_ID = "id_branch";
    SharedPreferences sharedPreferences;


    String StudentId;
    ImageButton BackPress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_passward_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sharedPreferences  = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        CurrentPassward = view.findViewById(R.id.current_passward_student1);
        NewPassward = view.findViewById(R.id.new_passward_student);
        Change1 = view.findViewById(R.id.change1student);
        ForgetPassward = view.findViewById(R.id.forget_password_student1);
        getActivity().findViewById(R.id.return_back111_backup).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.return_back111).setVisibility(View.GONE);
        BackPress = getActivity().findViewById(R.id.return_back111_backup);
        BackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().findViewById(R.id.return_back111_backup).setVisibility(View.GONE);
                getActivity().findViewById(R.id.return_back111).setVisibility(View.VISIBLE);


                getFragmentManager().popBackStack();


            }
        });

        StudentId = getArguments().getString("StudentId");


        Change1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentPassward.getText().toString().trim().isEmpty())
                {
                    CurrentPassward.setError("This Filed is Empty");
                }
                else if(NewPassward.getText().toString().trim().isEmpty())
                {
                    NewPassward.setError("This Filed is Empty");
                }
                else
                {
                    DocumentReference docIdRef = firebaseFirestore.collection("/Students/" ).document(StudentId.trim());
                    docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    if(document.getString("Passward").equals(CurrentPassward.getText().toString().trim()))
                                    {

                                        firebaseFirestore.collection("Students").document(StudentId.trim()).update("Passward" , NewPassward.getText().toString().trim());
                                        Toast.makeText(getActivity() , "Passward is Sucessfully Changed" , Toast.LENGTH_SHORT).show();
                                        getFragmentManager().popBackStack();
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

            }
        });
        ForgetPassward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //yet to impliment
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), ForgetPasswardStudentActivity.class);
                startActivity(intent);

            }
        });
    }
}