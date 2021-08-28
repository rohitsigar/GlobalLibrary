package com.example.globallibrary.Fragment;

import android.content.Context;
import android.net.Uri;
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

import com.example.globallibrary.Adpaters.adapterPost;
import com.example.globallibrary.Models.BranchOverview;
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

import pl.droidsonroids.gif.GifImageView;

public class FragmentAllBranches extends Fragment {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    StorageReference storageReference;

    private Uri filePath;
    String URL  = "";
    public RecyclerView recyclerView;
    public RecyclerView.Adapter _mAdapter;
    public RecyclerView.LayoutManager _layoutManager;

    GifImageView progressBar;

    public FragmentAllBranches() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_all_branches, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager _layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setFocusable(false);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        URL = "Branches/Branch1";
        ArrayList<BranchOverview> list = new ArrayList();

        progressBar = v.findViewById(R.id.progress_bar_8);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);


//        list.add(new BranchOverview("temp1" , "temp2" , "temp3", "temp4" ,URL ,"temp4"));
        firestore.collection("/Branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            private static final String TAG = "Rohit";


            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        URL = "Branches/" + document.getString("BranchName");

                        storageReference.child(URL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL for 'users/me/profile.png'
//                Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                                /// The string(file link) that you need
//
                                Uri downloadUrl = uri;
                                String s = downloadUrl.toString();
                                list.add(new BranchOverview(document.getString("BranchName") , document.getString("Discreption")
                                        , document.getString("LibraryAddress") ,document.getString("ContactNumber"),s ,document.getString("EmailAddress"), "" + String.valueOf(document.getLong("DefaultAmount"))));
                                Log.d(TAG, "onSuccess: checking" + list.size());

                                _mAdapter.notifyDataSetChanged();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });





                        ///  add items to the adapter
                    }
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);


                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(_layoutManager);
//        ///  add items to the adapter

        Log.d("TAG", "onSuccess: checking" + list.size());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(_layoutManager);
        ///  add items to the adapter
        _mAdapter = new adapterPost(list);
        ///  set Adapter to RecyclerView
        recyclerView.setAdapter(_mAdapter);
        return v;





    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
    public void workDone()
    {

        URL = "Branches/Branch1";
        ArrayList<BranchOverview> list = new ArrayList();
        firestore.collection("/Branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            private static final String TAG = "Rohit";


            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        URL = "Branches/" + document.getString("BranchName");

                        storageReference.child(URL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL for 'users/me/profile.png'
//                Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                                /// The string(file link) that you need
//
                                Uri downloadUrl = uri;
                                String s = downloadUrl.toString();
                                list.add(new BranchOverview(document.getString("BranchName") , document.getString("Discreption")
                                        , document.getString("LibraryAddress") ,document.getString("ContactNumber"),s ,document.getString("EmailAddress") , String.valueOf(document.getLong("DefaultAmount"))));
                                Log.d(TAG, "onSuccess: checking" + list.size());



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });





                        ///  add items to the adapter
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(_layoutManager);
        ///  add items to the adapter

        Log.d("TAG", "onSuccess: checking" + list.size());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(_layoutManager);
        ///  add items to the adapter
        _mAdapter = new adapterPost(list);
        ///  set Adapter to RecyclerView
        recyclerView.setAdapter(_mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}