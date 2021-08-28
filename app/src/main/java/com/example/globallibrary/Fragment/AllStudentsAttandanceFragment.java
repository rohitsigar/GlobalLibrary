package com.example.globallibrary.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Activity.StudentOverview;
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

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;

public class AllStudentsAttandanceFragment extends Fragment {
    public static AllStudentsAttandanceFragment newInstance(String s1) {
        AllStudentsAttandanceFragment allStudentsAttandanceFragment = new AllStudentsAttandanceFragment();
        Bundle args = new Bundle();
        args.putString("BranchId" , s1);
        allStudentsAttandanceFragment.setArguments(args);
        return allStudentsAttandanceFragment;
    }

    String BranchId;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage;

    public RecyclerView recyclerView;
    public ShortStudentsDetailsAdaptor adapter;
    public GridLayoutManager _layoutManager;

    StorageReference storageReference;
    ImageButton CanlenderDilog;
    GifImageView progressBar;
    TextView DateShow;

    ArrayList<ShortStudentDetails> list;
    ShortStudentsDetailsAdaptor.RecyclerViewClickListner listner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_students_attandance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BranchId = getArguments().getString("BranchId");

        recyclerView = (RecyclerView) view.findViewById(R.id.all_student_grid_attandance);
        _layoutManager = new GridLayoutManager(getActivity() , 3);
        recyclerView.setFocusable(false);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        DateShow = view.findViewById(R.id.showDateHere);
        CanlenderDilog = view.findViewById(R.id.changeDateUsingCalender);
        progressBar = view.findViewById(R.id.progress_bar_3);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        list = new ArrayList();
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        firestore.collection("/Branches/" + BranchId + "/StudentDetails").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            private static final String TAG = "Rohit";
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document1 : task.getResult())
                    {
                        String URL = "Student/" + document1.getId();
                        Log.d(TAG, "onComplete: checking123" + URL);


                        storageReference.child(URL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Uri downloadUrl = uri;
                                String s = downloadUrl.toString();
                                Log.d(TAG, "onSuccess: hello");
                                DocumentReference docIdRef = firestore.collection("Branches/" + BranchId + "/Attandance/").document(formattedDate);
                                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document2 = task.getResult();
                                            if (document2.exists()) {


                                                    if(document2.getBoolean(document1.getId().toString())!=null)
                                                    {
                                                        list.add(new ShortStudentDetails(document1.getString("FullName") , document1.getString("Discreption") , s , document1.getId() , "Green"));
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                    else
                                                    {
                                                        list.add(new ShortStudentDetails(document1.getString("FullName") , document1.getString("Discreption") , s , document1.getId() , "Red"));
                                                        adapter.notifyDataSetChanged();
                                                    }


                                                //existes

                                            } else {

                                                list.add(new ShortStudentDetails(document1.getString("FullName") , document1.getString("Discreption") , s , document1.getId() , "Red"));
                                                adapter.notifyDataSetChanged();
                                            }
                                        } else {

                                        }
                                    }
                                });

                                Log.d(TAG, "onSuccess: checkinghello" + list.size());



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                list.add(new ShortStudentDetails(document1.getString("FullName") , document1.getString("Discreption") , "NoImage" , document1.getId() , "Red"));
                                Log.d(TAG, "onSuccess: checking" + list.size());

                            }
                        });
                    }
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);


                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });




        DateShow.setText(formattedDate);

        CanlenderDilog.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog  = new DatePickerDialog(
                        getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = "";
                        if(day<10)
                        {
                            date = date + "0" + day;
                        }
                        else
                        {
                            date = date + day;
                        }
                        if(month<10)
                        {
                            date = date + "-0" + month;
                        }
                        else
                        {
                            date = date + "-" + month;
                        }
                        date = date + "-" + year;
                        DateShow.setText(date);
                        String finalDate = date;
                        DocumentReference docIdRef = firestore.collection("Branches/" + BranchId + "/Attandance/").document(finalDate);
                        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {


                                        for(ShortStudentDetails e : list)
                                        {
                                            Log.d("TAG", "onComplete:checking "   + e.getStudentId()  + document.getBoolean(e.StudentId.toString()));
                                            if(document.getBoolean(e.StudentId.toString())!=null)
                                            {
                                                e.Color = "Green";
                                                adapter.notifyDataSetChanged();
                                            }
                                            else
                                            {
                                                e.Color = "Red";
                                                adapter.notifyDataSetChanged();
                                            }
                                        }

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
                },year , month,day);
                datePickerDialog.show();
            }
        });




        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(_layoutManager);
        setOnClickListner();

        adapter = new ShortStudentsDetailsAdaptor(list , listner);
        ///  set Adapter to RecyclerView
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }
    private void setOnClickListner() {
        listner  = new ShortStudentsDetailsAdaptor.RecyclerViewClickListner() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getActivity() , StudentOverview.class);
                intent.putExtra("StudentId" ,list.get(position).getStudentId() );
                intent.putExtra("BranchId" , BranchId);
                startActivity(intent);
            }
        };

    }
}