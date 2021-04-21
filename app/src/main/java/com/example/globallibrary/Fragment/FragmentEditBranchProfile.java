package com.example.globallibrary.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FragmentEditBranchProfile extends Fragment {
    public static FragmentEditBranchProfile newInstance(String BranchName) {
        FragmentEditBranchProfile fragmentEditBranchProfile = new FragmentEditBranchProfile();
        Bundle args = new Bundle();
        args.putString("branchName", BranchName);
        fragmentEditBranchProfile.setArguments(args);
        return fragmentEditBranchProfile;
    }
    ImageButton TickButton;
    String BranchName;
    TextInputEditText OwnerName;
    TextInputEditText LibraryAddress;
    TextInputEditText ResidentialAddress;
    TextInputEditText Dob;
    TextInputEditText Disreption;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_branch_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TickButton = view.findViewById(R.id.tick_button_branch);
        LibraryAddress = view.findViewById(R.id.change_owner_library_address);
        ResidentialAddress = view.findViewById(R.id.change_owner_residential_address);
        OwnerName = view.findViewById(R.id.change_owner_name);
        Dob = view.findViewById(R.id.change_owner_dob);
        Disreption = view.findViewById(R.id.change_discreption);
        BranchName = getArguments().getString("branchName");
        Log.d("TAG", "onViewCreated: checking" + BranchName);
//        firebaseFirestore.collection("/Branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            private static final String TAG = "Rohit";
//
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        if(document.getString("BranchName").equals(BranchName))
//                        {
//                            Log.d(TAG, "onComplete: present" + BranchName);
//                            LibraryAddress.setText(document.getString("LibraryAddress"));
//                            ResidentialAddress.setText(document.getString("ResidentialAddress"));
//                            OwnerName.setText(document.getString("OwnerName"));
//                            Dob.setText(document.getString("DOB"));
//                        }
//                    }
//
//                } else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//            }
//        });
        TickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("/Branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "Rohit";

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("BranchName").equals(BranchName))
                                {
                                    if(!Dob.getText().toString().isEmpty())
                                    {
                                        firebaseFirestore.collection("/Branches").document(document.getId()).update("DOB" , Dob.getText().toString());
                                    }
                                    if(!ResidentialAddress.getText().toString().isEmpty())
                                    {
                                        firebaseFirestore.collection("/Branches").document(document.getId()).update("ResidentialAddress" , ResidentialAddress.getText().toString());
                                    }
                                    if(!LibraryAddress.getText().toString().isEmpty())
                                    {
                                        firebaseFirestore.collection("/Branches").document(document.getId()).update("LibraryAddress" , LibraryAddress.getText().toString());
                                    }
                                    if(!OwnerName.getText().toString().isEmpty())
                                    {
                                        firebaseFirestore.collection("/Branches").document(document.getId()).update("OwnerName" , OwnerName.getText().toString());
                                    }
                                    if(!Disreption.getText().toString().isEmpty())
                                    {
                                        firebaseFirestore.collection("/Branches").document(document.getId()).update("Discreption" , Disreption.getText().toString());
                                    }



                                    Toast.makeText(getActivity() , "Profile Updated" , Toast.LENGTH_SHORT).show();
                                    FragmentManager fm = getActivity().getSupportFragmentManager();
                                    fm.popBackStack();
                                }
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


            }
        });

    }
}