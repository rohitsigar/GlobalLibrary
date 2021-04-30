package com.example.globallibrary.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Adpaters.NotificationAdaptor;
import com.example.globallibrary.Models.NotificationDetails;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class NoticeStudentFragment extends Fragment {

    String StudentId;
    String BranchId;
    String branchName;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter _mAdapter;
    public RecyclerView.LayoutManager _layoutManager;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_notice_student, container, false);
        StudentId = getArguments().getString("StudentId");
        BranchId = getArguments().getString("BranchId");
        Log.d("TAG", "onCreateView: phoneNo" + StudentId);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_notification2);
     _layoutManager = new LinearLayoutManager(getContext());
        branchName = getArguments().getString("branchName");
        recyclerView.setFocusable(false);
        ArrayList<NotificationDetails> list = new ArrayList();
        CollectionReference docIdRef = firestore.collection("/Branches/" + BranchId.trim()+ "/Notifications/");
        docIdRef.orderBy("Sortthis" , Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            private static final String TAG = "Rohit";
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document2 : task.getResult()) {

                        list.add(new NotificationDetails(document2.getString("Title") , document2.getString("Discreption")
                                , document2.getString("Time") ,document2.getString("Date"),document2.getString("Day"),document2.getDate("SortThis")));
                        Log.d(TAG, "onSuccess: checking" + list.size());
                        _mAdapter.notifyDataSetChanged();
                    }


                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });




        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(_layoutManager);
        ///  add items to the adapter
        _mAdapter = new NotificationAdaptor(list);
        ///  set Adapter to RecyclerView
        recyclerView.setAdapter(_mAdapter);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}