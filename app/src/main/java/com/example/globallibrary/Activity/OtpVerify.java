package com.example.globallibrary.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;




public class OtpVerify extends AppCompatActivity {


    String otpId;
    private ProgressBar progressBar;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseAuthSettings firebaseAuthSettings = mAuth.getFirebaseAuthSettings();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private TextInputEditText otp;
    private Button reSendOtp;
    private Button signUpButton;
    String phoneNo;
    String fullName;
    String emailAddress;
    String residentialAddress;
    String Dob;
    String branchName;
    String passward;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("hello", "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);
        phoneNo = getIntent().getStringExtra("phoneNo");
        fullName = getIntent().getStringExtra("fullName");
        emailAddress = getIntent().getStringExtra("email");
        residentialAddress = getIntent().getStringExtra("address");
        Dob = getIntent().getStringExtra("dob");
        branchName = getIntent().getStringExtra("branch");
        passward = getIntent().getStringExtra("passward");
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        otp = findViewById(R.id.otp123);
        signUpButton = findViewById(R.id.Sign_up);
        reSendOtp = findViewById(R.id.resend_otp_branch);
        mAuth = FirebaseAuth.getInstance();
        Log.e("activity", "ok");
        initiateOtp();
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otp.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "otp is not written", Toast.LENGTH_LONG).show();
                } else if (otp.getText().toString().length() != 6) {
                    Toast.makeText(getApplicationContext(), "invalid OTP", Toast.LENGTH_LONG).show();
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId, otp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });


    }

    private void initiateOtp() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =  new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            otpId = s;
            Log.d("Rohit", "onVerificationCompleted:Code is 5");
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.d("Rohit", "onVerificationCompleted:Code is 2");
            signInWithPhoneAuthCredential(phoneAuthCredential);

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("Rohit", "onVerificationCompleted:Code is 3" + e.getMessage());

        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Map<String,Object> allDetails = new HashMap<>();
                            allDetails.put("FullName", fullName);
                            allDetails.put("ContactNumber", phoneNo);
                            allDetails.put("EmailAddress", emailAddress);
                            allDetails.put("ResidentialAddress", residentialAddress);
                            allDetails.put("DateOFBirth", Dob);
                            allDetails.put("BranchName", branchName);
                            Map<String,Object> studentSecurityDetails = new HashMap<>();
                            studentSecurityDetails.put("ContactNumber", phoneNo);
                            studentSecurityDetails.put("BranchName", branchName);
                            studentSecurityDetails.put("Passward" , passward);
                            firebaseFirestore.collection("Branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                private static final String TAG = "Rohit";
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if(branchName.equals(document.getString("BranchName")))
                                            {
                                                Log.d("rohit", "onComplete: " + document.getId() + " and branch name is " + branchName);
                                                String id1 = firebaseFirestore.collection("Branches").document(document.getId()).collection("StudentDetails").document().getId();
                                                firebaseFirestore.collection("Branches").document(document.getId()).collection("StudentDetails").document(id1).set(allDetails);
                                                firebaseFirestore.collection("Students").document(id1).set(studentSecurityDetails);
                                                // Sign in success, update UI with the signed-in user's information
                                                Intent intent = new Intent(OtpVerify.this, AuthenticationActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        }

                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

}