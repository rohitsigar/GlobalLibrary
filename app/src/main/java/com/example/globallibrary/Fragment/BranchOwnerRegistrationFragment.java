package com.example.globallibrary.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.globallibrary.Activity.AuthenticationActivity;
import com.example.globallibrary.Activity.GeneralActivity;
import com.example.globallibrary.Activity.OtpVerify;
import com.example.globallibrary.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class BranchOwnerRegistrationFragment extends Fragment {
    TextInputEditText ownerName;
    TextInputEditText ownerContactNumber;
    TextInputEditText ownerEmail;
    TextInputEditText ownerAddress;
    TextInputEditText libraryAddress;
    TextInputEditText dob;
    TextInputEditText branchName;
    TextInputEditText password;
    TextInputEditText rePassword;
    Button nextButton;
    public static String phoneNumber = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_branch_owner_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ownerName = view.findViewById(R.id.branch_owner_fullname);
        ownerContactNumber = view.findViewById(R.id.branch_owner_contact_number);
        ownerEmail = view.findViewById(R.id.branch_owner_email);
        ownerAddress = view.findViewById(R.id.branch_owner_address);
        libraryAddress = view.findViewById(R.id.branch_owner_library_address);
        dob = view.findViewById(R.id.branch_owner_dob);
        branchName = view.findViewById(R.id.branch_name);
        password = view.findViewById(R.id.branch_passward);
        rePassword = view.findViewById(R.id.branch_re_enter_passward);
        nextButton = view.findViewById(R.id.branch_registration_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = "+91";
                String number = ownerContactNumber.getText().toString();
//                if (number.isEmpty() || number.length() < 10) {
//                    ownerContactNumber.setError("Valid number is required");
//                    ownerContactNumber.requestFocus();
//                    return;
//                }
//                if(ownerName.getText().toString().isEmpty())
//                {
//                    ownerName.setError("Please Enter your Name");
//                    ownerName.requestFocus();
//                    return;
//                }
//                if(ownerEmail.getText().toString().isEmpty())
//                {
//                    ownerEmail.setError("Please Enter your Email");
//                    ownerEmail.requestFocus();
//                    return;
//                }
//                if(ownerAddress.getText().toString().isEmpty())
//                {
//                    ownerAddress.setError("Please Enter your Address");
//                    ownerAddress.requestFocus();
//                    return;
//                }
//                if(libraryAddress.getText().toString().isEmpty())
//                {
//                    libraryAddress.setError("Please Enter your Library Address");
//                    libraryAddress.requestFocus();
//                    return;
//                }
//                if(dob.getText().toString().isEmpty())
//                {
//                    dob.setError("Please Enter your Date of Birth");
//                    dob.requestFocus();
//                    return;
//                }
//                if(branchName.getText().toString().isEmpty())
//                {
//                    branchName.setError("Please Enter your Branch Name");
//                    ownerName.requestFocus();
//                    return;
//                }
//                if(password.getText().toString().isEmpty())
//                {
//                    ownerName.setError("Please Select Password");
//                    ownerName.requestFocus();
//                    return;
//                }
//                if(rePassword.getText().toString().isEmpty())
//                {
//                    rePassword.setError("Please re-Enter your Password");
//                    rePassword.requestFocus();
//                    return;
//                }
//                String p1 = password.getText().toString().trim();
//                String p2 = rePassword.getText().toString().trim();
////                if(p1!=p2)
////                {
////                    rePassword.setError("both password are not same");
////                    rePassword.requestFocus();
////                    return;
////                }
                phoneNumber = code + number;
                //Call the next activity and pass phone no with it
                Intent intent = new Intent(getActivity(), OtpVerify.class);
                intent.putExtra("phoneNo", phoneNumber);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(getActivity(), GeneralActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }
    }