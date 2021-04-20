package com.example.globallibrary.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class StudentPallet extends AppCompatActivity {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    StorageReference storageReference;
    String BranchName;

    private Uri filePath;
    String URL  = "";
    public RecyclerView recyclerView;
    public ShortStudentsDetailsAdaptor adapter;
    public RecyclerView.LayoutManager _layoutManager;



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
        Log.d("TAG", "onCreate: checking 1 2 3 " + BranchName);

        ArrayList<ShortStudentDetails> list = new ArrayList();
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
                                                    list.add(new ShortStudentDetails(document1.getString("FullName") , document1.getString("Discreption") , s));
                                                    Log.d(TAG, "onSuccess: checking" + list.size());

                                                    adapter.notifyDataSetChanged();


                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    list.add(new ShortStudentDetails(document1.getString("FullName") , document1.getString("Discreption") , "NoImage"));
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
        ///  add items to the adapter
        adapter = new ShortStudentsDetailsAdaptor(list);
        ///  set Adapter to RecyclerView
        recyclerView.setAdapter(adapter);
//        RecyclerView recyclerView = findViewById(R.id.Short_recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new ShortStudentsDetailsAdaptor(list);
////        adapter.setClickListener(this);
//        recyclerView.setAdapter(adapter);



    }
}