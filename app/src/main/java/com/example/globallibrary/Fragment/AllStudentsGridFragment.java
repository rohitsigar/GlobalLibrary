package com.example.globallibrary.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class AllStudentsGridFragment extends Fragment {
    public static AllStudentsGridFragment newInstance(String s1) {
        AllStudentsGridFragment allStudentsGridFragment = new AllStudentsGridFragment();
        Bundle args = new Bundle();
        args.putString("BranchId" , s1);
        allStudentsGridFragment.setArguments(args);
        return allStudentsGridFragment;
    }

    String BranchId;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage;

    public RecyclerView recyclerView;
    public ShortStudentsDetailsAdaptor adapter;
    public GridLayoutManager _layoutManager;

    StorageReference storageReference;

    ArrayList<ShortStudentDetails> list;
    GifImageView progressBar;
    ShortStudentsDetailsAdaptor.RecyclerViewClickListner listner;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_students_grid, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BranchId = getArguments().getString("BranchId");

        recyclerView = (RecyclerView) view.findViewById(R.id.all_student_grid);
         _layoutManager = new GridLayoutManager(getActivity() , 3);
        recyclerView.setFocusable(false);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        progressBar = view.findViewById(R.id.progress_bar_2);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);


        list = new ArrayList();
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
                                list.add(new ShortStudentDetails(document1.getString("FullName") , document1.getString("Discreption") , s , document1.getId() , "NoColor"));
                                Log.d(TAG, "onSuccess: checkinghello" + list.size());

                                adapter.notifyDataSetChanged();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                list.add(new ShortStudentDetails(document1.getString("FullName") , document1.getString("Discreption") , "NoImage" , document1.getId() , "NoColor"));
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