package com.example.globallibrary.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileBranchDetailsFragment extends Fragment {
    public static ProfileBranchDetailsFragment newInstance(String BranchName) {
        ProfileBranchDetailsFragment profileBranchDetailsFragment = new ProfileBranchDetailsFragment();
        Bundle args = new Bundle();
        args.putString("branchName", BranchName);
        profileBranchDetailsFragment.setArguments(args);
        return profileBranchDetailsFragment;
    }

    TextView Address;
    TextView EmailAddress;
    TextView ContactNumber;
    TextView OwnerName;
    String BranchName;
    String URL  = "";


    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile_branch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BranchName = getArguments().getString("branchName");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        URL = "Branches/" + BranchName;
        OwnerName = view.findViewById(R.id.owner_name);
        ContactNumber = view.findViewById(R.id.contact_number);
        Address = view.findViewById(R.id.library_address);
        EmailAddress = view.findViewById(R.id.email_address);
        firebaseFirestore.collection("/Branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            private static final String TAG = "Rohit";

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getString("BranchName").equals(BranchName))
                        {
                            Log.d(TAG, "onComplete: hua ?");
                            OwnerName.setText(document.getString("OwnerName"));
                            ContactNumber.setText(document.getString("ContactNumber"));
                            EmailAddress.setText(document.getString("EmailAddress"));
                            Address.setText(document.getString("LibraryAddress"));

                        }



                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });





    }


}