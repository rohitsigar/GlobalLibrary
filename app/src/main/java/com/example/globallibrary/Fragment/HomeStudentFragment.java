package com.example.globallibrary.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Activity.StudentAttandance;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
// pending
//1. store phone number locally in phone and keep student user loged in for now user have to log in every time

public class HomeStudentFragment extends Fragment {

    RecyclerView recyclerView;
    String StudentId;
    String branchId;
    TextView setQuote;
    MaterialButton showAttandance;
    MaterialButton MarkAttandance;
    MaterialButton MarkDone;
    LinearLayout Marked;
    LinearLayout NotMaked;
    LinearLayout MainDilog;


    TextView DisplayLocation;
    AlertDialog alertDialog;


    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_student_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        StudentId = getArguments().getString("StudentId");
        branchId = getArguments().getString("BranchId");

        MarkAttandance = view.findViewById(R.id.mark_attandance);
        MarkAttandance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ViewGroup viewGroup = getView().findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dilog_mark_attandance, viewGroup, false);


                //Now we need an AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);

                //finally creating the alert dialog and displaying it
                alertDialog = builder.create();


                alertDialog.show();
                MarkDone = alertDialog.findViewById(R.id.mark_done);
                Marked = alertDialog.findViewById(R.id.marked);
                NotMaked = alertDialog.findViewById(R.id.not_marked);
                MainDilog = alertDialog.findViewById(R.id.main_dilog);
                Marked.setVisibility(View.GONE);
                MainDilog.setVisibility(View.VISIBLE);
                NotMaked.setVisibility(View.GONE);
                MarkDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DocumentReference docIdRef = firestore.collection("/Branches/").document(branchId.trim());
                        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {

//                                        GeoPoint geoPoint  = document.getGeoPoint("Location");
//                                        int Radius = Integer.valueOf((Integer) document.get("Radius"));
//                                        Location location = null;
//                                        Location location1 = new Location("");
//                                        location1.setLongitude(geoPoint.getLongitude());
//                                        location1.setLatitude(geoPoint.getLatitude());
//                                        double dist = location.distanceTo(location1);
//                                        if(dist < Radius)
//                                        {
                                            Marked.setVisibility(View.VISIBLE);
                                            MainDilog.setVisibility(View.GONE);
                                            Date c = Calendar.getInstance().getTime();
                                            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                            String formattedDate = df.format(c);
                                            String studentDoc = formattedDate.substring(3,10);
                                            String doc = formattedDate.substring(0,2);
                                        Log.d("TAG", "onComplete: check 1" + studentDoc + " " + doc + " " + formattedDate);
                                        DocumentReference docIdRef = firestore.collection("/Branches/" + branchId.trim() + "/Attandance/" ).document(formattedDate.trim());
                                        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        firestore.collection("Branches").document(branchId).collection("Attandance").document(formattedDate).update(StudentId , true);



                                                    } else {
                                                        Map<String,Object> allDetails = new HashMap<>();
                                                        allDetails.put(StudentId, true);
                                                        firestore.collection("Branches").document(branchId).collection("Attandance").document(formattedDate).set(allDetails);

                                                        Log.d("TAG", "onComplete: not possible" );

                                                    }
                                                } else {

                                                }
                                            }
                                        });
                                        DocumentReference docIdRef1 = firestore.collection("/Branches/" + branchId.trim() + "/StudentDetails/"  + StudentId.trim() + "/Attandance/").document(studentDoc.trim());
                                        docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        firestore.collection("Branches").document(branchId).collection("StudentDetails").document(StudentId).collection("Attandance").document(studentDoc.trim()).update(doc , true);




                                                    } else {
                                                        Map<String,Object> allDetails = new HashMap<>();
                                                        allDetails.put(doc, true);
                                                        firestore.collection("Branches").document(branchId).collection("StudentDetails").document(StudentId).collection("Attandance").document(studentDoc).set(allDetails);

                                                        Log.d("TAG", "onComplete: not possible" );

                                                    }
                                                } else {

                                                }
                                            }
                                        });

//                                        }
//                                        else {
//                                            NotMaked.setVisibility(View.VISIBLE);
//                                            MainDilog.setVisibility(View.GONE);
//
//
//                                        }


                                    } else {

                                        Log.d("TAG", "onComplete: not possible" );

                                    }
                                } else {

                                }
                            }
                        });
                    }
                });


            }
        });


        Log.d("phone", "onViewCreated: " + StudentId); //getting unique phone number from sigh in page.
        setQuote = view.findViewById(R.id.show_quote);
        showAttandance = view.findViewById(R.id.show_attandance);
        showAttandance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , StudentAttandance.class);
                intent.putExtra("StudentId" , StudentId);
                intent.putExtra("BranchId" , branchId);
                startActivity(intent);
            }
        });
        DocumentReference docIdRef1 = firestore.collection("/Branches/" ).document(branchId.trim());
        docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                        setQuote.setText(document.getString("Quote"));

                    } else {

                        Log.d("TAG", "onComplete: not possible" );

                    }
                } else {

                }
            }
        });




    }
}