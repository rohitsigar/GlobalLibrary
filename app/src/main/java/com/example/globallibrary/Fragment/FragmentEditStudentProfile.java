package com.example.globallibrary.Fragment;

import android.os.Bundle;
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

public class FragmentEditStudentProfile extends Fragment {
    public static FragmentEditStudentProfile newInstance(String PhoneNo) {
        FragmentEditStudentProfile fragmentEditstudentProfile = new FragmentEditStudentProfile();
        Bundle args = new Bundle();
        args.putString("PhoneNo", PhoneNo);
        fragmentEditstudentProfile.setArguments(args);
        return fragmentEditstudentProfile;
    }
    ImageButton TickButton;
    String BranchName;
    TextInputEditText StudentName;
    TextInputEditText EmailAddress;
    TextInputEditText ResidentialAddress;
    TextInputEditText Dob;
    TextInputEditText ContactNumber;
    TextInputEditText Discreption;



    String PhoneNo = "";
    FirebaseFirestore firebaseFirestore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_student_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseFirestore = FirebaseFirestore.getInstance();
        PhoneNo = getArguments().getString("PhoneNo");
        StudentName = view.findViewById(R.id.change_student_name_edit_profile_student);
        ResidentialAddress = view.findViewById(R.id.change_student_residential_address_edit_student_profile);
        EmailAddress  = view.findViewById(R.id.change_student_email_address_edit_student_profile);
        Dob = view.findViewById(R.id.change_student_dob_edit_student_profile);
        ContactNumber = view.findViewById(R.id.change_student_number_edit_student_profile);
        Discreption = view.findViewById(R.id.change_discreption_edit_student_profile);

        TickButton = view.findViewById(R.id.tick_button_student);
        PhoneNo  = getArguments().getString("PhoneNo");
        TickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                                                           if(!ContactNumber.getText().toString().isEmpty())
                                                                           {
                                                                               firebaseFirestore.collection("Branches/" + document2.getId() + "/StudentDetails/").document(document3.getId()).update("ContactNumber" , ContactNumber.getText().toString() );
                                                                           }
                                                                            if(!StudentName.getText().toString().isEmpty())
                                                                            {
                                                                                firebaseFirestore.collection("Branches/" + document2.getId() + "/StudentDetails/").document(document3.getId()).update("FullName" , StudentName.getText().toString() );
                                                                            }
                                                                            if(!EmailAddress.getText().toString().isEmpty())
                                                                            {
                                                                                firebaseFirestore.collection("Branches/" + document2.getId() + "/StudentDetails/").document(document3.getId()).update("EmailAddress" , EmailAddress.getText().toString() );
                                                                            }
                                                                            if(!ResidentialAddress.getText().toString().isEmpty())
                                                                            {
                                                                                firebaseFirestore.collection("Branches/" + document2.getId() + "/StudentDetails/").document(document3.getId()).update("ResidentialAddress" , ResidentialAddress.getText().toString() );
                                                                            }
                                                                            if(!Dob.getText().toString().isEmpty())
                                                                            {
                                                                                firebaseFirestore.collection("Branches/" + document2.getId() + "/StudentDetails/").document(document3.getId()).update("DateOfBirth" , Dob.getText().toString() );
                                                                            }
                                                                            if(!Discreption.getText().toString().isEmpty())
                                                                            {
                                                                                firebaseFirestore.collection("Branches/" + document2.getId() + "/StudentDetails/").document(document3.getId()).update("Discreption" , Discreption.getText().toString() );
                                                                            }

                                                                            Toast.makeText(getActivity() , "Profile Is Updated " , Toast.LENGTH_SHORT).show();
                                                                            FragmentManager fm = getActivity().getSupportFragmentManager();
                                                                            fm.popBackStack();

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
        });





    }
}