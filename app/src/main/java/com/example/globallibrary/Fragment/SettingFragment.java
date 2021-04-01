package com.example.globallibrary.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

public class SettingFragment extends Fragment {

     Button ChangePhoneNumber;
     Button ChangePassward;
     Button EditProfile;
     Button SubmitButtonChangePassward;
     String BranchName;
     Button ChangeMethodToPasswardChangePhoneNo;
     Button ChangeMethodToPhoneNoChangePhoneNo;
     Button ChangeMethodToPhoneNoChangePassward;
     Button ChangeMethodToPasswardChangePassward;
     Button OtpSendForPasswardChange;
     Button OtpSendForPhoneNoChange;
     TextInputEditText GetCurrentPasswardChangePassward;
     TextInputEditText GetCurrentPasswardChangePhoneNo;
     TextInputEditText GetNewNumber;
     TextInputEditText GetNewPassward;
     String phoneNo;
     String OtpID;
     TextInputEditText GetCurrentPhoneNOChangePhoneNo;
     TextInputEditText OtpChangePhoneNo;
     TextInputEditText OtpChangePassward;
     TextInputEditText GetCurrentPhoneNoChangePassward;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseAuthSettings firebaseAuthSettings = mAuth.getFirebaseAuthSettings();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ChangePassward =view.findViewById(R.id.change_passward_setting);
        ChangePhoneNumber = view.findViewById(R.id.change_phone_number_setting);
        EditProfile = view.findViewById(R.id.edit_profile_setting);
       BranchName = getArguments().getString("branchName");
       SubmitButtonChangePassward = view.findViewById(R.id.submit_change_passward12345);
       ChangeMethodToPasswardChangePhoneNo = view.findViewById(R.id.change_method_to_passward_change_phone_number);
       ChangeMethodToPhoneNoChangePhoneNo = view.findViewById(R.id.change_method_to_phone_number_change_phone_number);
        ChangeMethodToPasswardChangePassward = view.findViewById(R.id.change_method_to_passward_change_passward);
        ChangeMethodToPhoneNoChangePassward = view.findViewById(R.id.change_method_to_phone_number_change_passward);
        OtpSendForPasswardChange = view.findViewById(R.id.otp_send_change_passward_method);
        OtpSendForPhoneNoChange = view.findViewById(R.id.otp_send_change_phone_no_method);
        GetCurrentPasswardChangePassward = view.findViewById(R.id.branch_passward_change_passward);
        GetCurrentPasswardChangePhoneNo = view.findViewById(R.id.branch_passward_change_phone_number);
        GetNewNumber = view.findViewById(R.id.branch_new_phone_number_change_phone_number);
        GetCurrentPhoneNOChangePhoneNo = view.findViewById(R.id.branch_phone_number_change_phone_number);
        OtpChangePassward = view.findViewById(R.id.otp_input_change_passward123);
        GetCurrentPhoneNoChangePassward = view.findViewById(R.id.branch_phone_number_change_passward);
        GetNewPassward = view.findViewById(R.id.branch_new_passward_change_passward);

        mAuth = FirebaseAuth.getInstance();
        OtpChangePhoneNo = view.findViewById(R.id.otp_input_change_phone_no);
        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EditBranchProfile();
                Bundle bundle = new Bundle();
                bundle.putString("branchName", BranchName);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_setting,fragment).addToBackStack(null).commit();

            }
        });
        ChangePhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(view.findViewById(R.id.change_phone_number_method).getVisibility()== View.GONE)
                {
                    view.findViewById(R.id.change_phone_number_method).setVisibility(View.VISIBLE);
                }
                else
                {
                    view.findViewById(R.id.change_phone_number_method).setVisibility(View.GONE);
                }
            }
        });
//        SubmitButtonChangePassward.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("TAG", "onClick: Finally");
//            }
//        });
        ChangePassward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(view.findViewById(R.id.change_passward_method).getVisibility()==View.GONE)
                {
                    view.findViewById(R.id.change_passward_method).setVisibility(View.VISIBLE);
                }
                else
                {
                    view.findViewById(R.id.change_passward_method).setVisibility(View.GONE);
                }
            }
        });

        ChangeMethodToPasswardChangePhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.branch_phone_number_change_phone_number_for_visibility).setVisibility(View.GONE);
                view.findViewById(R.id.branch_passward_change_phone_number_for_visibility).setVisibility(View.VISIBLE);
                ChangeMethodToPasswardChangePhoneNo.setVisibility(View.GONE);
                ChangeMethodToPhoneNoChangePhoneNo.setVisibility(View.VISIBLE);
                OtpSendForPhoneNoChange.setVisibility(View.GONE);
                view.findViewById(R.id.submit_change_phone_no).setVisibility(View.VISIBLE);

            }
        });
        ChangeMethodToPhoneNoChangePhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.branch_phone_number_change_phone_number_for_visibility).setVisibility(View.VISIBLE);
                view.findViewById(R.id.branch_passward_change_phone_number_for_visibility).setVisibility(View.GONE);
                ChangeMethodToPasswardChangePhoneNo.setVisibility(View.VISIBLE);
                ChangeMethodToPhoneNoChangePhoneNo.setVisibility(View.GONE);
                OtpSendForPhoneNoChange.setVisibility(View.VISIBLE);
                view.findViewById(R.id.submit_change_phone_no).setVisibility(View.GONE);

            }
        });
        ChangeMethodToPhoneNoChangePassward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.branch_phone_number_change_passward_for_visibility).setVisibility(View.VISIBLE);
                view.findViewById(R.id.branch_passward_change_passward_for_visibility).setVisibility(View.GONE);
                ChangeMethodToPasswardChangePassward.setVisibility(View.VISIBLE);
                ChangeMethodToPhoneNoChangePassward.setVisibility(View.GONE);
                OtpSendForPasswardChange.setVisibility(View.VISIBLE);
                view.findViewById(R.id.submit_change_passward12345).setVisibility(View.GONE);


            }
        });
        ChangeMethodToPasswardChangePassward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.branch_phone_number_change_passward_for_visibility).setVisibility(View.GONE);
                view.findViewById(R.id.branch_passward_change_passward_for_visibility).setVisibility(View.VISIBLE);
                ChangeMethodToPasswardChangePassward.setVisibility(View.GONE);
                ChangeMethodToPhoneNoChangePassward.setVisibility(View.VISIBLE);
                OtpSendForPasswardChange.setVisibility(View.GONE);
                view.findViewById(R.id.submit_change_passward12345).setVisibility(View.VISIBLE);

            }
        });
        OtpSendForPhoneNoChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.otp_input_change_phone_no_visibility).setVisibility(View.VISIBLE);
                view.findViewById(R.id.branch_phone_number_change_phone_number_for_visibility).setVisibility(View.GONE);
                phoneNo = GetCurrentPhoneNOChangePhoneNo.getText().toString();
                view.findViewById(R.id.submit_change_phone_no).setVisibility(View.VISIBLE);
                OtpSendForPhoneNoChange.setVisibility(View.GONE);

                initiateOtp();





            }
        });
        OtpSendForPasswardChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.otp_input_change_passward_for_visibility).setVisibility(View.VISIBLE);
                view.findViewById(R.id.branch_phone_number_change_passward_for_visibility).setVisibility(View.GONE);
                view.findViewById(R.id.submit_change_passward12345).setVisibility(View.VISIBLE);
                OtpSendForPasswardChange.setVisibility(View.GONE);
                phoneNo = GetCurrentPhoneNoChangePassward.getText().toString();
                initiateOtp();

            }
        });
        view.findViewById(R.id.submit_change_passward12345).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: chalo yeh to chal rha hai");
                if(ChangeMethodToPasswardChangePassward.getVisibility()==View.GONE)
                {
                    firebaseFirestore.collection("/BranchAuth").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        private static final String TAG = "Rohit";

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult())
                                    if (document.getString("BranchName").equals(BranchName)) {

                                        if(GetCurrentPasswardChangePassward.getText().toString().equals(document.getString("Passward")))
                                        {

                                            firebaseFirestore.collection("BranchAuth").document(document.getId()).update("Passward" , GetNewPassward.getText().toString());
                                            view.findViewById(R.id.change_passward_method).setVisibility(View.GONE);
                                        }
                                        else
                                        {
                                            GetCurrentPasswardChangePassward.setError("Wrong Passward");
                                        }

                                    }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
                }
                else
                {

                    if (OtpChangePassward.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "otp is not written", Toast.LENGTH_LONG).show();
                    } else if (OtpChangePassward.getText().toString().length() != 6) {
                        Toast.makeText(getActivity(), "invalid OTP", Toast.LENGTH_LONG).show();
                    } else {
                        Log.d("TAG", "onClick: checking" + OtpChangePassward.getText().toString());
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OtpID, OtpChangePassward.getText().toString());
                        signInWithPhoneAuthCredential(credential);
                    }
                }
            }
        });
        view.findViewById(R.id.submit_change_phone_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ChangeMethodToPasswardChangePhoneNo.getVisibility()==View.GONE)
                {


                    firebaseFirestore.collection("/BranchAuth").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        private static final String TAG = "Rohit";

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult())
                                    if (document.getString("BranchName").equals(BranchName)) {
                                        if(document.getString("Passward").equals(GetCurrentPasswardChangePhoneNo.getText().toString()))
                                        {
                                            firebaseFirestore.collection("/Branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                private static final String TAG = "Rohit";

                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult())
                                                            if (document.getString("BranchName").equals(BranchName)) {
                                                               firebaseFirestore.collection("Branches").document(document.getId()).update("ContactNumber" , GetNewNumber.getText().toString());
                                                               view.findViewById(R.id.change_phone_number_method).setVisibility(View.GONE);

                                                            }

                                                    } else {
                                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });
                                        }
                                        else
                                        {
                                            GetCurrentPasswardChangePhoneNo.setError("Wrong Passward");
                                        }


                                    }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });


                }
                else
                {
                    if (OtpChangePhoneNo.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "otp is not written", Toast.LENGTH_LONG).show();
                    } else if (OtpChangePhoneNo.getText().toString().length() != 6) {
                        Toast.makeText(getActivity(), "invalid OTP", Toast.LENGTH_LONG).show();
                    } else {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OtpID, OtpChangePhoneNo.getText().toString());
                        signInWithPhoneAuthCredential(credential);
                    }
                }

            }
        });



    }
    private void initiateOtp() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =  new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            OtpID = s;
            Log.d("Rohit", "onVerificationCompleted:Code is 5");
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.d("Rohit", "onVerificationCompleted:Code is 2");
            signInWithPhoneAuthCredential(phoneAuthCredential);

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Log.d("Rohit", "onVerificationCompleted:Code is 3" + e.getMessage());

        }
    };
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            if(GetNewNumber.getText().toString()!=null) {


                                firebaseFirestore.collection("Branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    private static final String TAG = "Rohit";

                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                if (BranchName.equals(document.getString("BranchName"))) {


                                                    firebaseFirestore.collection("Branches").document(document.getId()).update("ContactNumber", GetNewNumber.getText().toString());
                                                    getView().findViewById(R.id.change_phone_number_method).setVisibility(View.GONE);


                                                }
                                            }

                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                            }
                            else {
                                firebaseFirestore.collection("BranchAuth").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    private static final String TAG = "Rohit";

                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                if (BranchName.equals(document.getString("BranchName"))) {

                                                    firebaseFirestore.collection("BranchAuth").document(document.getId()).update("Passward", GetNewPassward.getText().toString());
                                                    getView().findViewById(R.id.change_passward_method).setVisibility(View.GONE);


                                                }
                                            }

                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                            }

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.d("TAG", "Error getting documents: ", task.getException());

                        }
                    }
                });
    }
}