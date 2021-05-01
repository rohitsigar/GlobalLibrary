package com.example.globallibrary.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

public class ForgetPasswardBranchActivity extends AppCompatActivity {

    TextInputEditText ContactNumber;
    TextInputEditText Otp;
    TextInputEditText NewPassward;
    TextInputLayout ContactLay ;
    TextInputLayout OtpLay;
    TextInputLayout PasswardLay;
    Button ResetPassward;


    private static final String KEY_ACCESS = "access";
    private static final String SHARED_PREF = "PREF";
    private static final String KEY_BRANCH_ID = "id";
    private static final String KEY_STUDENT_ID = "id_student";
    private static final String KEY_STUDENT_B_ID = "id_branch";
    SharedPreferences sharedPreferences;


    Button CheckOtp;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseAuthSettings firebaseAuthSettings = mAuth.getFirebaseAuthSettings();
    String BranchId;
    Button SendOtp;
    Button resendOtp;

    private String verificationId;
    FirebaseFirestore firebaseFirestore  = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_passward_branch);

        ContactNumber = findViewById(R.id.phone_number_forget_passward2);
        Otp = findViewById(R.id.otp122);
        NewPassward = findViewById(R.id.new_passward_branch_activity);
        SendOtp = findViewById(R.id.send_otp1);
        resendOtp = findViewById(R.id.resend_otp_2);
        ResetPassward = findViewById(R.id.ResetPassward1);
        ContactLay = findViewById(R.id.a1);
        OtpLay = findViewById(R.id.b1);
        PasswardLay = findViewById(R.id.c1);
        CheckOtp = findViewById(R.id.check_otp1);

        sharedPreferences  = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

        SendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("Branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "Rohit";
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean b = false;;
                            for (QueryDocumentSnapshot document : task.getResult()) {



                                if(document.getString("ContactNumber").equals(ContactNumber.getText().toString()))
                                {
                                    b = true;
                                    BranchId = document.getId();
                                    sendVerificationCode(ContactNumber.getText().toString());
                                }
                            }
                            if(!b)
                            {
                                Toast.makeText(ForgetPasswardBranchActivity.this , "This number is Not Registred : " , Toast.LENGTH_LONG).show();

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.commit();
                                finish();
                                Intent intent = new Intent(ForgetPasswardBranchActivity.this , AuthenticationActivity.class);
                                startActivity(intent);
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

                ContactLay.setVisibility(View.GONE);
                OtpLay.setVisibility(View.VISIBLE);
                CheckOtp.setVisibility(View.VISIBLE);
                SendOtp.setVisibility(View.GONE);

            }
        });
        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode(ContactNumber.getText().toString());
            }
        });

        CheckOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(Otp.getText().toString())) {
                    // if the OTP text field is empty display
                    // a message to user to enter OTP
                    Toast.makeText(ForgetPasswardBranchActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    // if OTP field is not empty calling
                    // method to verify the OTP.
                    verifyCode(Otp.getText().toString());
                }
            }
        });
        ResetPassward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("BranchAuth").document(BranchId).update("Passward" , NewPassward.getText().toString());
                Toast.makeText(ForgetPasswardBranchActivity.this , "Passward Changed Sucessfully" , Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                finish();
                Intent intent = new Intent(ForgetPasswardBranchActivity.this , AuthenticationActivity.class);
                startActivity(intent);
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

                            CheckOtp.setVisibility(View.GONE);
                            PasswardLay.setVisibility(View.VISIBLE);
                            ResetPassward.setVisibility(View.VISIBLE);
                            OtpLay.setVisibility(View.GONE);


                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(ForgetPasswardBranchActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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

                                final String code = phoneAuthCredential.getSmsCode();

                                // checking if the code
                                // is null or not.
                                if (code != null) {
                                    // if the code is not null then
                                    // we are setting that code to
                                    // our OTP edittext field.
                                    Otp.setText(code);

                                    // after setting this code
                                    // to OTP edittext field we
                                    // are calling our verifycode method.
                                    verifyCode(code);
                                }

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(ForgetPasswardBranchActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
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

    @Override
    public void onBackPressed() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        finish();
        Intent intent = new Intent(ForgetPasswardBranchActivity.this , AuthenticationActivity.class);
        startActivity(intent);
    }
}