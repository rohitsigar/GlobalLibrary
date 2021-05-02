package com.example.globallibrary.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class StudentOverview extends AppCompatActivity {

    ImageView StudentImage;
    TextView StudentName;
    TextView Discreption;
    TextView BranchName;
    TextView EmailAddress;
    TextView ContactNumber;
    TextView ResidentialAddress;
    TextView DOB;
    String StudentId = "";
    String BranchId = "";
    FirebaseStorage storage;
    StorageReference storageReference;
    ImageButton Back;
    MaterialButton ChangeFee;
    AlertDialog alertDialog;
    MaterialButton DeleteAccount;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_overview);
        StudentImage  = findViewById(R.id.id_Profile_Image_student);
        StudentName = findViewById(R.id.id_fullName_TextView_student_overview);
        Discreption = findViewById(R.id.student_discreption_oveview);
        BranchName  = findViewById(R.id.branch_name_student_overview);
        EmailAddress = findViewById(R.id.email_address_student_overview);
        DeleteAccount = findViewById(R.id.delete_account);
        ContactNumber = findViewById(R.id.contact_number_student_profile_overview);
        ResidentialAddress = findViewById(R.id.residential_address_student_profile_overview);
        ChangeFee = findViewById(R.id.change_student_fee);
        Intent intent = getIntent();
        StudentId  = intent.getExtras().getString("StudentId");
        BranchId = intent.getExtras().getString("BranchId");
        DeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(StudentOverview.this).inflate(R.layout.delete_account_dilog, viewGroup, false);


                //Now we need an AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentOverview.this);

                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);

                //finally creating the alert dialog and displaying it
                alertDialog = builder.create();
                alertDialog.show();
                MaterialButton Permanentdelete = alertDialog.findViewById(R.id.delete_permanent);
                Permanentdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        firebaseFirestore.collection("Branches/" + BranchId + "/StudentDetails/" ).document(StudentId)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                        Toast.makeText(StudentOverview.this,"Account Delete Successfully" , Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error deleting document", e);
                                    }
                                });
                        firebaseFirestore.collection("Students").document(StudentId)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error deleting document", e);
                                    }
                                });


                        alertDialog.dismiss();
                    }
                });

            }
        });
        ChangeFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(StudentOverview.this).inflate(R.layout.change_student_fee_dilog, viewGroup, false);


                //Now we need an AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentOverview.this);

                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);

                //finally creating the alert dialog and displaying it
                alertDialog = builder.create();
                alertDialog.show();
                EditText AmountFee = alertDialog.findViewById(R.id.amount_change_student);
                MaterialButton Change = alertDialog.findViewById(R.id.button_change_student);
                Log.d("TAG", "onClick: studentId : " + StudentId + " BranchId  : " + BranchId);

                DocumentReference docIdRef = firebaseFirestore.collection("Branches/"  + BranchId + "/StudentDetails" ).document(StudentId);
                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                AmountFee.setText(String.valueOf(document.getDouble("Amount")));
                            } else {

                                Log.d("TAG", "onComplete: not possible" );

                            }
                        } else {

                        }
                    }
                });
                Change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(AmountFee.getText().toString().trim().isEmpty())
                        {
                            AmountFee.setError("This Field is Empty");
                        }
                        else
                        {
                            firebaseFirestore.collection("Branches/"  + BranchId + "/StudentDetails" ).document(StudentId).update("Amount" ,Double.parseDouble(AmountFee.getText().toString().trim()));
                            Toast.makeText(StudentOverview.this , "Fee is Sucessfully Changed to " +AmountFee.getText().toString()  , Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
        DOB = findViewById(R.id.dob_student_overview);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Back  = findViewById(R.id.return_to_studentPallet);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DocumentReference docIdRef = firebaseFirestore.collection("Branches/"  + BranchId + "/StudentDetails" ).document(StudentId);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document3 = task.getResult();
                    if (document3.exists()) {

                        ContactNumber.setText(document3.getString("ContactNumber"));
                        BranchName.setText(document3.getString("BranchName"));
                        DOB.setText(document3.getString("DateOFBirth"));
                        ResidentialAddress.setText(document3.getString("ResidentialAddress"));
                        EmailAddress.setText(document3.getString("EmailAddress"));
                        StudentName.setText(document3.getString("FullName"));
                        Discreption.setText(document3.getString("Discreption"));
                        String URL = "Student/" + document3.getId();
                        Log.d("TAG", "onComplete: checking123" + URL);


                        storageReference.child(URL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Uri downloadUrl = uri;
                                String s = downloadUrl.toString();
                                Picasso.get().load(s).into(StudentImage);



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                StudentImage.setImageResource(R.drawable.student_logo);


                                // Handle any errors
                            }
                        });




                    } else {

                    }
                } else {

                }
            }
        });




    }
}