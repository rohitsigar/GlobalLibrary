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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileStudentDetailsFragment extends Fragment {
    public static ProfileStudentDetailsFragment newInstance(String PhoneNo) {
        ProfileStudentDetailsFragment profileStudentDetailsFragment = new ProfileStudentDetailsFragment();
        Bundle args = new Bundle();
        args.putString("PhoneNo", PhoneNo);
        profileStudentDetailsFragment.setArguments(args);
        return profileStudentDetailsFragment;
    }

    TextView BranchName;
    TextView DOB;
    TextView ResidentialAddress;
    TextView ContactNumber;
    TextView EmailAddress;
    String PhoneNo = "";
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_student_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BranchName  = view.findViewById(R.id.branch_name_student_profile);
        DOB  = view.findViewById(R.id.dob_student_profile);
        ResidentialAddress = view.findViewById(R.id.residential_address_student_profile);
        ContactNumber = view.findViewById(R.id.contact_number_student_profile);
        EmailAddress = view.findViewById(R.id.email_address_student_profile);
        PhoneNo  = getArguments().getString("PhoneNo");
        firebaseFirestore.collection("Students/").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot doucment1 : task.getResult())
                    {
                        if(doucment1.getString("ContactNumber").equals(PhoneNo))
                        {
                            firebaseFirestore.collection("Branches/").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful())
                                    {
                                        for(QueryDocumentSnapshot document2 : task.getResult())
                                        {
                                            if(document2.getString("BranchName").equals(doucment1.getString("BranchName")))
                                            {
                                                firebaseFirestore.collection("Branches/" + document2.getId() + "/StudentDetails/").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            for(QueryDocumentSnapshot document3 : task.getResult())
                                                            {
                                                                if(document3.getString("ContactNumber").equals(PhoneNo))
                                                                {
                                                                    ContactNumber.setText(document3.getString("ContactNumber"));
                                                                    BranchName.setText(document3.getString("BranchName"));
                                                                    DOB.setText(document3.getString("DateOFBirth"));
                                                                    ResidentialAddress.setText(document3.getString("ResidentialAddress"));
                                                                    EmailAddress.setText(document3.getString("EmailAddress"));
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                }

            }
        });



    }
}