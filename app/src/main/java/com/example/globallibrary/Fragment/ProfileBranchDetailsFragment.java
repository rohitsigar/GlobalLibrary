package com.example.globallibrary.Fragment;

import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileBranchDetailsFragment extends Fragment {
    public static ProfileBranchDetailsFragment newInstance(String BranchId) {
        ProfileBranchDetailsFragment profileBranchDetailsFragment = new ProfileBranchDetailsFragment();
        Bundle args = new Bundle();
        args.putString("branchId", BranchId);
        profileBranchDetailsFragment.setArguments(args);
        return profileBranchDetailsFragment;
    }

    TextView Address;
    TextView EmailAddress;
    TextView ContactNumber;
    TextView OwnerName;
    String BranchId;



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
        BranchId = getArguments().getString("branchId");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        OwnerName = view.findViewById(R.id.owner_name);
        ContactNumber = view.findViewById(R.id.contact_number);
        Address = view.findViewById(R.id.library_address);
        EmailAddress = view.findViewById(R.id.email_address);
        DocumentReference docIdRef = firebaseFirestore.collection("Branches/" ).document(BranchId);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                        OwnerName.setText(document.getString("OwnerName"));
                        ContactNumber.setText(document.getString("ContactNumber"));
                        EmailAddress.setText(document.getString("EmailAddress"));
                        Address.setText(document.getString("LibraryAddress"));

                    } else {

                    }
                } else {

                }
            }
        });






    }


}