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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentEditBranchProfile extends Fragment {
    public static FragmentEditBranchProfile newInstance(String BranchId) {
        FragmentEditBranchProfile fragmentEditBranchProfile = new FragmentEditBranchProfile();
        Bundle args = new Bundle();
        args.putString("branchId", BranchId);
        fragmentEditBranchProfile.setArguments(args);
        return fragmentEditBranchProfile;
    }
    ImageButton TickButton;
    String BranchId;
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
        BranchId = getArguments().getString("branchId");
        Log.d("TAG", "onViewCreated: checking" + BranchId);

        TickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docIdRef = firebaseFirestore.collection("Branches/" ).document(BranchId);
                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
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