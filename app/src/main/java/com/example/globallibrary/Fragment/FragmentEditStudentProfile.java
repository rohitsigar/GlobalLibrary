package com.example.globallibrary.Fragment;

import android.content.Intent;
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

import com.example.globallibrary.Activity.GeneralActivity;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentEditStudentProfile extends Fragment {
    public static FragmentEditStudentProfile newInstance(String[] s1) {
        FragmentEditStudentProfile fragmentEditstudentProfile = new FragmentEditStudentProfile();
        Bundle args = new Bundle();
        args.putString("StudentId", s1[0]);
        args.putString("BranchId", s1[1]);
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


    String StudentId = "";
    String BranchId;
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
        StudentId = getArguments().getString("StudentId");
        BranchId = getArguments().getString("BranchId");
        Log.d("TAG", "onViewCreated: StudentId " + StudentId + " BranchId "  + BranchId);
        StudentName = view.findViewById(R.id.change_student_name_edit_profile_student);
        ResidentialAddress = view.findViewById(R.id.change_student_residential_address_edit_student_profile);
        EmailAddress = view.findViewById(R.id.change_student_email_address_edit_student_profile);
        Dob = view.findViewById(R.id.change_student_dob_edit_student_profile);
        ContactNumber = view.findViewById(R.id.change_student_number_edit_student_profile);
        Discreption = view.findViewById(R.id.change_discreption_edit_student_profile);

        TickButton = view.findViewById(R.id.tick_button_student);
        TickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference docIdRef = firebaseFirestore.collection("/Branches/" + BranchId.trim() + "/StudentDetails/").document(StudentId.trim());
                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {


                                if (!ContactNumber.getText().toString().isEmpty()) {
                                    firebaseFirestore.collection("Branches/" + BranchId.trim() + "/StudentDetails/").document(StudentId.trim()).update("ContactNumber", ContactNumber.getText().toString());
                                }
                                if (!StudentName.getText().toString().isEmpty()) {
                                    firebaseFirestore.collection("Branches/" + BranchId.trim() + "/StudentDetails/").document(StudentId.trim()).update("FullName", StudentName.getText().toString());
                                }
                                if (!EmailAddress.getText().toString().isEmpty()) {
                                    firebaseFirestore.collection("Branches/" + BranchId.trim() + "/StudentDetails/").document(StudentId.trim()).update("EmailAddress", EmailAddress.getText().toString());
                                }
                                if (!ResidentialAddress.getText().toString().isEmpty()) {
                                    firebaseFirestore.collection("Branches/" + BranchId.trim() + "/StudentDetails/").document(StudentId.trim()).update("ResidentialAddress", ResidentialAddress.getText().toString());
                                }
                                if (!Dob.getText().toString().isEmpty()) {
                                    firebaseFirestore.collection("Branches/" + BranchId.trim() + "/StudentDetails/").document(StudentId.trim()).update("DateOfBirth", Dob.getText().toString());
                                }
                                if (!Discreption.getText().toString().isEmpty()) {
                                    firebaseFirestore.collection("Branches/" + BranchId.trim() + "/StudentDetails/").document(StudentId.trim()).update("Discreption", Discreption.getText().toString());
                                }

                                Toast.makeText(getActivity(), "Profile Is Updated ", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity() , GeneralActivity.class);
                                startActivity(intent);


                            } else {

                                Log.d("TAG", "onComplete: not possible");

                            }
                        } else {

                        }
                    }
                });


            }
        });
    }
}
