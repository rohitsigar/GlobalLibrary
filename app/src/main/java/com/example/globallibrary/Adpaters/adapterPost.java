package com.example.globallibrary.Adpaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Models.BranchOverview;
import com.example.globallibrary.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;
public class adapterPost  extends RecyclerView.Adapter<adapterPost.ViewHolder> {
    private LayoutInflater mInflater;
    private List<BranchOverview> mData;
    FirebaseStorage storage;
    StorageReference storageReference;


    public adapterPost(List<BranchOverview> Data) {
        mData = Data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.branch_details, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        // set values for each item
        BranchOverview itam = mData.get(position);
        viewHolder.BranchName.setText(itam.BranchName);
        viewHolder.Discreption.setText(itam.Discreption);
        viewHolder.Address.setText(String.valueOf(itam.Address));
        viewHolder.ContactNumber.setText(itam.ContactNumber);
        viewHolder.EmailAddress.setText(itam.EmailAddress);

        Picasso.get().load(itam.PhotoURL).into(viewHolder.BranchPhoto);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
       public TextView BranchName , Discreption , Address , ContactNumber, EmailAddress ;
       public ImageView BranchPhoto;


        public ViewHolder(View v) {
            super(v);
            BranchName = (TextView) v.findViewById(R.id.branch_name_all_branches);
            Discreption = (TextView) v.findViewById(R.id.branch_discreption_all_branches);
            Address = (TextView) v.findViewById(R.id.library_address_all_branches);
            ContactNumber = (TextView) v.findViewById(R.id.contact_number_all_branches);
            EmailAddress = (TextView) v.findViewById(R.id.email_address_all_branches);
            BranchPhoto = (ImageView) v.findViewById(R.id.branch_image_all_branches);
        }
    }
}