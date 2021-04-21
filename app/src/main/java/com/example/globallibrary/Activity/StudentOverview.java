package com.example.globallibrary.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class StudentOverview extends AppCompatActivity {

    com.mikhaellopez.circularimageview.CircularImageView StudentImage;
    TextView StudentName;
    TextView Discreption;
    TextView BranchName;
    TextView EmailAddress;
    TextView ContactNumber;
    TextView ResidentialAddress;
    TextView DOB;
    String PhoneNo = "";
    FirebaseStorage storage;
    StorageReference storageReference;
    ImageButton Back;

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
        ContactNumber = findViewById(R.id.contact_number_student_profile_overview);
        ResidentialAddress = findViewById(R.id.residential_address_student_profile_overview);
        DOB = findViewById(R.id.dob_student_overview);
        Intent intent = getIntent();
        PhoneNo  = intent.getExtras().getString("PhoneNo");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Back  = findViewById(R.id.return_to_studentPallet);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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
                                                                    StudentName.setText(document3.getString("FullName"));
                                                                    Discreption.setText(document3.getString("Discreption"));
                                                                    String URL = "Student/" + document3.getId();
                                                                    Log.d("TAG", "onComplete: checking123" + URL);


                                                                    storageReference.child(URL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                        @Override
                                                                        public void onSuccess(Uri uri) {
                                                                            // Got the download URL for 'users/me/profile.png'
//                Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                                                                            /// The string(file link) that you need
//
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