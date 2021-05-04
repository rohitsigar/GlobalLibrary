package com.example.globallibrary.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Activity.GeneralActivity;
import com.example.globallibrary.Adpaters.FeeDetailsBranchSideAdaptor;
import com.example.globallibrary.Models.FeeDetailsBranchSide;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class FeeBranchFragment extends Fragment {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String BranchId;

    FirebaseStorage storage;
    StorageReference storageReference;

    AlertDialog alertDialog;

    public RecyclerView recyclerView;
    public FeeDetailsBranchSideAdaptor adapter;
    public RecyclerView.LayoutManager _layoutManager;

    ArrayList<FeeDetailsBranchSide> list;
    FeeDetailsBranchSideAdaptor.RecyclerViewClickListner listner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_fee_branch, container, false);
        BranchId = getArguments().getString("BranchId");
        recyclerView = (RecyclerView) v.findViewById(R.id.branch_fee_recyclerview);
        RecyclerView.LayoutManager _layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setFocusable(false);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        list = new ArrayList();
        firestore.collection("/Branches/" + BranchId +"/Fee").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            private static final String TAG = "Rohit";

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document1 : task.getResult())
                    {

                        DocumentReference docIdRef = firestore.collection("/Branches/" + BranchId.trim() + "/StudentDetails" ).document(document1.getString("StudentId").trim());
                        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {

                                        String URL = "Student/" + document1.getString("StudentId");
                                        Log.d(TAG, "onComplete: checking123" + URL);


                                        storageReference.child(URL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                Uri downloadUrl = uri;
                                                String s1 = downloadUrl.toString();
                                                Log.d(TAG, "onSuccess: hello");
                                                String s = String.valueOf( Math.round(document1.getDouble("Date") ))+ "-" + String.valueOf( Math.round(document1.getDouble("Month")) )+ "-" +String.valueOf( Math.round(document1.getDouble("Year")) );
                                                list.add(new FeeDetailsBranchSide(document1.getString("StudentId") , document1.getDouble("Amount")  ,s , BranchId , document1.getId() , document1.getBoolean("Status")  , document.getString("FullName") , s1));
                                                adapter.notifyDataSetChanged();

                                                Log.d(TAG, "onSuccess: checkinghello" + list.size());




                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {

                                                Log.d(TAG, "onSuccess: checking" + list.size());
                                                String s = String.valueOf( Math.round(document1.getDouble("Date") ))+ "-" + String.valueOf( Math.round(document1.getDouble("Month")) )+ "-" +String.valueOf( Math.round(document1.getDouble("Year")) );
                                                list.add(new FeeDetailsBranchSide(document1.getString("StudentId") , document1.getDouble("Amount")  ,s , BranchId , document1.getId() , document1.getBoolean("Status")  , document.getString("FullName") , "NOImage"));
                                                adapter.notifyDataSetChanged();



                                            }
                                        });






                                    } else {

                                        Log.d("TAG", "onComplete: not possible" );

                                    }
                                } else {

                                }
                            }
                        });


                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });






        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(_layoutManager);
        setOnClickListner();

        adapter = new FeeDetailsBranchSideAdaptor(list , listner);
        ///  set Adapter to RecyclerView
        recyclerView.setAdapter(adapter);

        return v;
    }
    private void setOnClickListner() {
        listner  = new FeeDetailsBranchSideAdaptor.RecyclerViewClickListner() {
            @Override
            public void onClick(View v, int position) {

                ViewGroup viewGroup = getView().findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.mark_as_paid_dilog, viewGroup, false);


                //Now we need an AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);

                //finally creating the alert dialog and displaying it
                alertDialog = builder.create();
                alertDialog.show();
                MaterialButton MarkPaid = alertDialog.findViewById(R.id.mark_paid);
                MarkPaid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FeeDetailsBranchSide feeDetailsBranchSide = list.get(position);
                        firestore.collection("Branches").document(BranchId).collection("Fee").document(feeDetailsBranchSide.UniquePaymentId).update("Status" , true);
                        firestore.collection("Branches").document(BranchId).collection("StudentDetails").document(feeDetailsBranchSide.StudentId).collection("Fee").document(feeDetailsBranchSide.UniquePaymentId).update("Status" , true);
                        Toast.makeText(getActivity() , "Fee Paid SucessFully" , Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        Intent intent = new Intent(getActivity(), GeneralActivity.class);
                        startActivity(intent);
                    }
                });




            }
        };

    }
}