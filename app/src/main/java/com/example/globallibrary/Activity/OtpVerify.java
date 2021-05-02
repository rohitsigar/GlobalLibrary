package com.example.globallibrary.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.android.material.button.MaterialButton;
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

import java.util.Calendar;
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
    private MaterialButton signUpButton;
    String phoneNo;
    String fullName;
    String emailAddress;
    String residentialAddress;
    String Dob;
    String branchName;
    String passward;
    private String verificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("hello", "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);
        phoneNo = getIntent().getStringExtra("phoneNo");
        Log.d("rohit", "onCreate: " + phoneNo);
        fullName = getIntent().getStringExtra("fullName");
        emailAddress = getIntent().getStringExtra("email");
        residentialAddress = getIntent().getStringExtra("address");
        Dob = getIntent().getStringExtra("dob");
        branchName = getIntent().getStringExtra("branch");
        passward = getIntent().getStringExtra("passward");
        progressBar = findViewById(R.id.progressbar1);
        otp = findViewById(R.id.otp123);
        signUpButton = findViewById(R.id.Sign_up);
        reSendOtp = findViewById(R.id.resend_otp_branch);
        reSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode(phoneNo);

            }
        });
        mAuth = FirebaseAuth.getInstance();
        Log.e("activity", "ok");
        sendVerificationCode(phoneNo);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.getProgress();
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating if the OTP text field is empty or not.
                if (TextUtils.isEmpty(otp.getText().toString())) {
                    // if the OTP text field is empty display
                    // a message to user to enter OTP
                    Toast.makeText(OtpVerify.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    // if OTP field is not empty calling
                    // method to verify the OTP.
                    verifyCode(otp.getText().toString());
                }
            }
        });


    }
    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Calendar calendar  = Calendar.getInstance();
                            int month = calendar.get(Calendar.MONTH);
                            int date = calendar.get(Calendar.DATE);
                            int year = calendar.get(Calendar.YEAR);
                            Map<String,Object> allDetails = new HashMap<>();
                            allDetails.put("FullName", fullName);
                            allDetails.put("ContactNumber", phoneNo);
                            allDetails.put("EmailAddress", emailAddress);
                            allDetails.put("ResidentialAddress", residentialAddress);
                            allDetails.put("DateOFBirth", Dob);
                            allDetails.put("BranchName", branchName);
                            allDetails.put("JoinDate" , date);
                            allDetails.put("JoinMonth" , month);
                            allDetails.put("JoinYear" , year);
                            Map<String,Object> studentSecurityDetails = new HashMap<>();
                            studentSecurityDetails.put("ContactNumber", phoneNo);

                            studentSecurityDetails.put("Passward" , passward);
                            firebaseFirestore.collection("Branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                private static final String TAG = "Rohit";
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if(branchName.equals(document.getString("BranchName")))
                                            {
                                                studentSecurityDetails.put("BranchId", document.getId());
                                                allDetails.put("Amount" , document.getDouble("DefaultAmount"));
                                                Log.d("rohit", "onComplete: " + document.getId() + " and branch name is " + branchName);
                                                String id1 = firebaseFirestore.collection("Branches").document(document.getId()).collection("StudentDetails").document().getId();
                                                firebaseFirestore.collection("Branches").document(document.getId()).collection("StudentDetails").document(id1).set(allDetails);
                                                firebaseFirestore.collection("Students").document(id1).set(studentSecurityDetails);


                                                Map<String ,Object> Fee  = new HashMap<>();
                                                Fee.put("StudentId" , id1);
                                                Fee.put("Date" , date);
                                                Fee.put("Month" , month+1);
                                                Fee.put("Year" , year);
                                                Fee.put("Status" , false);
                                                Fee.put("Amount" , document.getDouble("DefaultAmount"));
                                                Map<String ,Object> Fee1  = new HashMap<>();
                                                Fee1.put("Date" , date);
                                                Fee1.put("Month" , month+1);
                                                Fee1.put("Year" , year);
                                                Fee1.put("Status" , false);
                                                Fee1.put("Amount" , document.getDouble("DefaultAmount"));
                                                String id2 = id1 + "-" + String.valueOf(month+1) + "-"  + String.valueOf(year);
                                                firebaseFirestore.collection("Branches").document(document.getId()).collection("Fee").document(id2).set(Fee);
                                                firebaseFirestore.collection("Branches").document(document.getId()).collection("StudentDetails").document(id1).collection("Fee").document(id2).set(Fee1);

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
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            Intent i = new Intent(OtpVerify.this, AuthenticationActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(OtpVerify.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void sendVerificationCode(String number) {
        // this method is used for getting
//         OTP on user phone
        PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber("+91" + number)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(this)                 // Activity (for callback binding)
                                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        progressBar.setVisibility(View.GONE);
                                        final String code = phoneAuthCredential.getSmsCode();

                                        // checking if the code
                                        // is null or not.
                                        if (code != null) {
                                            // if the code is not null then
                                            // we are setting that code to
                                            // our OTP edittext field.
                                            otp.setText(code);

                                            // after setting this code
                                            // to OTP edittext field we
                                            // are calling our verifycode method.
                                            verifyCode(code);
                                        }

                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(OtpVerify.this, e.getMessage(), Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        progressBar.setVisibility(View.GONE);
                                        super.onCodeSent(s, forceResendingToken);
                                        verificationId = s;

                                    }
                                })          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }




}