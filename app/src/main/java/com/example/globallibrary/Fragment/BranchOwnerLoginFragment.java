package com.example.globallibrary.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.globallibrary.Activity.GeneralActivity;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class BranchOwnerLoginFragment extends Fragment {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    Button signUpButton;
    Button loginButton;
    TextInputEditText branchName;

    TextInputEditText passward;
    boolean test = false;

    private static final String KEY_ACCESS = "access";
    private static final String SHARED_PREF = "PREF";
    private static final String KEY_BRANCH_ID = "id";
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_branch_owner_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signUpButton = view.findViewById(R.id.sign_up_branch_owner);
        loginButton = view.findViewById(R.id.go_button_branch_owner);
        branchName = view.findViewById(R.id.branch_branch_name);
        passward = view.findViewById(R.id.branch_owner_password);
        sharedPreferences  = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        String loged  = sharedPreferences.getString(KEY_ACCESS , null);
        if(loged!=null)
        {
            Intent intent = new Intent(getActivity(), GeneralActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("BranchAuth").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "Rohit";

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String s = document.getString("BranchName");
                                if(s.equals(branchName.getText().toString()))
                                {
                                    String p  = document.getString("Passward");
                                    if(p.equals(passward.getText().toString()))
                                    {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(KEY_ACCESS , "branchAccess");
                                        editor.putString(KEY_BRANCH_ID , document.getId().trim());
                                        Log.d("TAG", "onComplete: checking " + document.getId());
                                        editor.apply();
                                        Intent intent = new Intent(getActivity(), GeneralActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        passward.setError("Incorrect Passward");
                                        test = true;
                                    }
                                }

                            }
                            if(!test) {
                                branchName.setError("Branch Does not exist");
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Fragment fragment = new BranchOwnerRegistrationFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(1, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
}