package com.example.globallibrary.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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

import java.util.HashMap;
import java.util.Map;

public class EditBranchProfile extends Fragment {
    public static EditBranchProfile newInstance(String BranchName) {
        EditBranchProfile editBranchProfile = new EditBranchProfile();
        Bundle args = new Bundle();
        args.putString("branchName", BranchName);
        editBranchProfile.setArguments(args);
        return editBranchProfile;
    }
    ImageButton TickButton;
    String BranchName;
    TextInputEditText OwnerName;
    TextInputEditText LibraryAddress;
    TextInputEditText ResidentialAddress;
    TextInputEditText Dob;
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
        BranchName = getArguments().getString("branchName");
        Log.d("TAG", "onViewCreated: checking" + BranchName);
        firebaseFirestore.collection("/Branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            private static final String TAG = "Rohit";

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getString("BranchName").equals(BranchName))
                        {
                            Log.d(TAG, "onComplete: present" + BranchName);
                            LibraryAddress.setText(document.getString("LibraryAddress"));
                            ResidentialAddress.setText(document.getString("ResidentialAddress"));
                            OwnerName.setText(document.getString("OwnerName"));
                            Dob.setText(document.getString("DOB"));
                        }
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
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
                                    Log.d(TAG, "onComplete: yesssss");
                                    Map<String,Object> UpdateProfile = new HashMap<>();
                                    UpdateProfile.put("DOB" , Dob.getText().toString());
                                    UpdateProfile.put("ResidentialAddress" , ResidentialAddress.getText().toString());
                                    UpdateProfile.put("LibraryAddress" , LibraryAddress.getText().toString());
                                    UpdateProfile.put("OwnerName" , OwnerName.getText().toString());

                                    firebaseFirestore.collection("/Branches").document(document.getId()).update(UpdateProfile);
                                }
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
//                Fragment fragment = new ProfileBranchFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString(BranchName , "branchName");
//                fragment.setArguments(bundle);
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container, fragment);
//                fragmentTransaction.commit();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

    }
}