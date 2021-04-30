package com.example.globallibrary.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Activity.StudentPallet;
import com.example.globallibrary.Adpaters.BranchReportAdaptor;
import com.example.globallibrary.Models.ReportQuizBranchItem;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class HomeBranchFragment extends Fragment {

    MaterialButton Quote;
    AlertDialog alertDialog;
    EditText QuoteText;
    MaterialButton SendQuote;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter _mAdapter;
    public RecyclerView.LayoutManager _layoutManager;
    FirebaseStorage storage;
    StorageReference storageReference;
    TextView textView;


    MaterialButton StudentPallet1;
String branchId;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.home_branch_fragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.report_quiz_branch);
        RecyclerView.LayoutManager _layoutManager = new LinearLayoutManager(getContext());
        textView = v.findViewById(R.id.text_view_1);
        recyclerView.setFocusable(false);
        ArrayList<ReportQuizBranchItem> list = new ArrayList();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        branchId = getArguments().getString("branchId");
        firebaseFirestore.collection("/Branches/" + branchId + "/QuizReport").orderBy("Sortthis" , Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            private static final String TAG = "Rohit";
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if(!task.getResult().isEmpty())
                    {
                        textView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        textView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        DocumentReference documentReference = firebaseFirestore.collection("Branches").document(branchId).collection("StudentDetails").document(document.getString("StudentId"));
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document1 = task.getResult();
                                    if (document1.exists()) {
                                       String  URL = "Student/" + document1.getId();
                                        Log.d(TAG, "onComplete: checking123" + URL);


                                        storageReference.child(URL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                Uri downloadUrl = uri;
                                                String s = downloadUrl.toString();
                                                Log.d(TAG, "onSuccess: hello");

                                                Log.d(TAG, "onSuccess: checkinghello" + list.size());
                                                list.add(new ReportQuizBranchItem(document1.getString("FullName") , document.getString("Performance") , document.getString("Catigory") , document.getString("Difficulty") , document.getString("Day") , document.getString("Date"),document.getString("Time"),s));
                                                _mAdapter.notifyDataSetChanged();



                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {

                                                Log.d(TAG, "onSuccess: checking" + list.size());
                                                list.add(new ReportQuizBranchItem(document1.getString("FullName") , document.getString("Performance") , document.getString("Catigory") , document.getString("Difficulty") , document.getString("Day") , document.getString("Date"),document.getString("Time") , "NoImage"));
                                                _mAdapter.notifyDataSetChanged();



                                            }
                                        });









                                    } else {

                                    }
                                }


                            }
                        });


                    }


                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(_layoutManager);
        ///  add items to the adapter
        _mAdapter = new BranchReportAdaptor(list);
        ///  set Adapter to RecyclerView
        recyclerView.setAdapter(_mAdapter);

        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Quote = view.findViewById(R.id.Quote);
        branchId = getArguments().getString("branchId");
        Log.d("TAG", "onViewCreated: checkhigh" + getArguments().getString("branchId").trim());
        StudentPallet1 = view.findViewById(R.id.student_pallet);
        StudentPallet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), StudentPallet.class);
                intent.putExtra("branchId", branchId);
                startActivity(intent);
            }
        });
        Quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewGroup viewGroup = getView().findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dilog_set_quote, viewGroup, false);


                //Now we need an AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);

                //finally creating the alert dialog and displaying it
                alertDialog = builder.create();
                alertDialog.show();
                QuoteText = alertDialog.findViewById(R.id.quote);
                SendQuote = alertDialog.findViewById(R.id.sendQuote);
                SendQuote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.d("TAG", "onClick: check " + branchId);
                        DocumentReference docIdRef = firebaseFirestore.collection("Branches/" ).document(branchId);

                        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {

                                        if(QuoteText.getText().toString().isEmpty())
                                        {
                                            QuoteText.setError("Field Empty");
                                            Toast.makeText(getActivity() , "Please Enter Quote First!" , Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            firebaseFirestore.collection("Branches").document(branchId.trim()).update("Quote" ,QuoteText.getText().toString() );
                                            Toast.makeText(getActivity() , "Quote is Send!" , Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                        }




                                    } else {

                                    }
                                } else {

                                }
                            }
                        });

                    }
                });



            }
        });

    }
}