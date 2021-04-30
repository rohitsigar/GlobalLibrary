package com.example.globallibrary.Fragment;

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

import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ChangePasswardBranchFragment extends Fragment {

    TextInputEditText CurrentPassward;
    TextInputEditText NewPassward;


    MaterialButton Change1;
    Button ForgetPassward;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ImageButton Back;





    String BranchId;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_passward_branch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CurrentPassward = view.findViewById(R.id.current_passward_1_branch);
        NewPassward = view.findViewById(R.id.new_passward_branch);
        Change1 = view.findViewById(R.id.change1_branch);
        ForgetPassward = view.findViewById(R.id.forget_password_branch);

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

                                    firebaseFirestore.collection("BranchAuth").document(BranchId.trim()).update("Passward" , NewPassward.getText().toString().trim());
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