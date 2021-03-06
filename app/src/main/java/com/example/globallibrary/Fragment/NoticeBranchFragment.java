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

import pl.droidsonroids.gif.GifImageView;


public class NoticeBranchFragment extends Fragment {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    public RecyclerView recyclerView;
    public RecyclerView.Adapter _mAdapter;
    public RecyclerView.LayoutManager _layoutManager;


    String branchId;
    GifImageView progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_notice_branch, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_notification);
        RecyclerView.LayoutManager _layoutManager = new LinearLayoutManager(getContext());
        branchId = getArguments().getString("branchId");
        String s = "/Branches/" + branchId + "/Notifications/";
        recyclerView.setFocusable(false);
        ArrayList<NotificationDetails> list = new ArrayList();
        CollectionReference docIdRef = firestore.collection(s );

        progressBar = v.findViewById(R.id.progress_bar_5);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        docIdRef.orderBy("Sortthis" , Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            private static final String TAG = "Rohit";


            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        list.add(new NotificationDetails(document.getString("Title") , document.getString("Discreption")
                                , document.getString("Time") ,document.getString("Date"),document.getString("Day") , document.getDate("SortThis")));
                        Log.d(TAG, "onSuccess: checking" + list.size());
                        _mAdapter.notifyDataSetChanged();







                        ///  add items to the adapter
                    }
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);



                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });;




        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(_layoutManager);
        ///  add items to the adapter
        _mAdapter = new NotificationAdaptor(list);
        ///  set Adapter to RecyclerView
        recyclerView.setAdapter(_mAdapter);
        return  v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        branchId = getArguments().getString("branchId");

    }
}