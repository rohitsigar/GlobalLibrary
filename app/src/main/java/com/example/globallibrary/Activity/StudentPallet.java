package com.example.globallibrary.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Adpaters.ShortStudentsDetailsAdaptor;
import com.example.globallibrary.Models.ShortStudentDetails;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StudentPallet extends AppCompatActivity {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    StorageReference storageReference;
    String BranchName;
    ImageButton Back;
    ImageButton Attandance;
    ImageButton StudentPallet;
    TextView HeadLine;
    LinearLayout DatePallet;
    TextView DateShow;

    private Uri filePath;
    String URL  = "";
    public RecyclerView recyclerView;
    public ShortStudentsDetailsAdaptor adapter;
    public RecyclerView.LayoutManager _layoutManager;

    ArrayList<ShortStudentDetails> list;
    ShortStudentsDetailsAdaptor.RecyclerViewClickListner listner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_pallet);
        recyclerView = (RecyclerView) findViewById(R.id.Short_recyclerView);
        RecyclerView.LayoutManager _layoutManager = new LinearLayoutManager(this);
        recyclerView.setFocusable(false);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Intent intent = getIntent();
        BranchName = intent.getStringExtra("branchName");
        Back = findViewById(R.id.return_to_home12);
        Attandance = findViewById(R.id.attandance_student_pallet);
        StudentPallet = findViewById(R.id.home_student_pallet);
        HeadLine  = findViewById(R.id.mainname);
        DatePallet = findViewById(R.id.DatePallet);
        DateShow = findViewById(R.id.showDateHere);



        Attandance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Attandance.setVisibility(View.GONE);
                StudentPallet.setVisibility(View.VISIBLE);
                HeadLine.setVisibility(View.GONE);
                DatePallet.setVisibility(View.VISIBLE);
//                java.util.Date currentTime = Calendar.getInstance().getTime();
//                String time  = currentTime.toString().trim();
//                String date =  time.substring(8,10) +" "+ time.substring(4,7) +" " + time.substring(30,34) ;
//                HashMap<String, Integer> map1 = null;
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                firestore.collection("Branches/").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot document : task.getResult())
                            {
                                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                                DocumentReference docIdRef = rootRef.collection("Branches/" + document.getId() + "/Attandance/").document(formattedDate);
                                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                  //existes

                                            } else {
                                               //not existss
                                                for(ShortStudentDetails e : list)
                                                {
                                                   e.Color = "Red";
                                                    adapter.notifyDataSetChanged();

                                                }
                                            }
                                        } else {

                                        }
                                    }
                                });
                            }
                        }

                    }
                });



                DateShow.setText(formattedDate);


            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        Log.d("TAG", "onCreate: checking 1 2 3 " + BranchName);

       list = new ArrayList();
        firestore.collection("/Branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            private static final String TAG = "Rohit";

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getString("BranchName").equals(BranchName))
                        {
                            String id1 = document.getId();
                            Log.d(TAG, "onComplete: checking" + id1);
                            firestore.collection("/Branches/" + document.getId().toString() + "/StudentDetails").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                private static final String TAG = "Rohit";

                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document1 : task.getResult())
                                        {
                                            URL = "Student/" + document1.getId();
                                            Log.d(TAG, "onComplete: checking123" + URL);
                                           

                                            storageReference.child(URL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    // Got the download URL for 'users/me/profile.png'
//                Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                                                    /// The string(file link) that you need
//
                                                    Uri downloadUrl = uri;
                                                    String s = downloadUrl.toString();
                                                    Log.d(TAG, "onSuccess: hello");
                                                    list.add(new ShortStudentDetails(document1.getString("FullName") , document1.getString("Discreption") , s , document1.getString("ContactNumber") , "NoColor"));
                                                    Log.d(TAG, "onSuccess: checkinghello" + list.size());

                                                    adapter.notifyDataSetChanged();


                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    list.add(new ShortStudentDetails(document1.getString("FullName") , document1.getString("Discreption") , "NoImage" , document1.getString("ContactNumber") , "NoColor"));
                                                    Log.d(TAG, "onSuccess: checking" + list.size());


                                                    adapter.notifyDataSetChanged();

                                                    // Handle any errors
                                                }
                                            });
                                        }

                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                        }
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(_layoutManager);
//        ///  add items to the adapter
//        adapter = new ShortStudentsDetailsAdaptor(list);
//        ///  set Adapter to RecyclerView
//        recyclerView.setAdapter(adapter);
        Log.d("TAG", "onCreate: hello guys");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(_layoutManager);
        setOnClickListner();
        ///  add items to the adapter
        adapter = new ShortStudentsDetailsAdaptor(list , listner);
        ///  set Adapter to RecyclerView
        recyclerView.setAdapter(adapter);




    }

    private void setOnClickListner() {
        listner  = new ShortStudentsDetailsAdaptor.RecyclerViewClickListner() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(StudentPallet.this , StudentOverview.class);
                intent.putExtra("PhoneNo" ,list.get(position).getPhoneNo() );
                startActivity(intent);
            }
        };

    }
}