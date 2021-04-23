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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileStudentDetailsFragment extends Fragment {
    public static ProfileStudentDetailsFragment newInstance(String[] s1) {
        ProfileStudentDetailsFragment profileStudentDetailsFragment = new ProfileStudentDetailsFragment();
        Bundle args = new Bundle();
        args.putString("StudentId", s1[0]);
        args.putString("BranchId" , s1[1]);
        profileStudentDetailsFragment.setArguments(args);
        return profileStudentDetailsFragment;
    }

    TextView BranchName;
    TextView DOB;
    TextView ResidentialAddress;
    TextView ContactNumber;
    TextView EmailAddress;
    String StudentId = "";
    String BranchId;
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
        StudentId  = getArguments().getString("StudentId");
        BranchId = getArguments().getString("BranchId");
        DocumentReference docIdRef = firebaseFirestore.collection("/Branches/" + BranchId.trim() + "/StudentDetails/" ).document(StudentId.trim());
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                        ContactNumber.setText(document.getString("ContactNumber"));
                        BranchName.setText(document.getString("BranchName"));
                        DOB.setText(document.getString("DateOFBirth"));
                        ResidentialAddress.setText(document.getString("ResidentialAddress"));
                        EmailAddress.setText(document.getString("EmailAddress"));



                    } else {

                        Log.d("TAG", "onComplete: not possible" );

                    }
                } else {

                }
            }
        });



    }
}