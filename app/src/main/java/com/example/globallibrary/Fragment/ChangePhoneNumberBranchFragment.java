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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.globallibrary.Activity.ForgetPasswardBranchActivity;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ChangePhoneNumberBranchFragment extends Fragment {



    TextInputEditText CurrentPassward;
    TextInputEditText NewNumber1;


    MaterialButton Change1;
    Button ForgetPassward;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();



    private static final String KEY_ACCESS = "access";
    private static final String SHARED_PREF = "PREF";
    private static final String KEY_BRANCH_ID = "id";
    SharedPreferences sharedPreferences;


    String BranchId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_phone_number_branch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //part1

        sharedPreferences  = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

        CurrentPassward = view.findViewById(R.id.current_passward_1_branch1);
        NewNumber1 = view.findViewById(R.id.new_phone_no_branch);
        Change1 = view.findViewById(R.id.change1_branch1);
        ForgetPassward = view.findViewById(R.id.forget_password_branch12);

        BranchId = getArguments().getString("BranchId");


        Change1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docIdRef = firebaseFirestore.collection("/BranchAuth/" ).document(BranchId.trim());
                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if(document.getString("Passward")==CurrentPassward.getText().toString().trim())
                                {

                                       firebaseFirestore.collection("Branches").document(BranchId.trim()).update("ContactNumber" , NewNumber1.getText().toString().trim());
                                    Toast.makeText(getActivity() , "Contact Number is Sucessfully Changed" , Toast.LENGTH_SHORT);
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
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), ForgetPasswardBranchActivity.class);
                startActivity(intent);
            }
        });





    }
}