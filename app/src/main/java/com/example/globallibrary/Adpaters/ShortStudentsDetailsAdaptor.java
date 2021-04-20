
package com.example.globallibrary.Adpaters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Models.ShortStudentDetails;
import com.example.globallibrary.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ShortStudentsDetailsAdaptor extends RecyclerView.Adapter<ShortStudentsDetailsAdaptor.ViewHolder> {
    private LayoutInflater mInflater;
    private List<ShortStudentDetails> mData;
    FirebaseStorage storage;
    StorageReference storageReference;

    public ShortStudentsDetailsAdaptor(List<ShortStudentDetails> Data) {
        mData = Data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.student_short_details, viewGroup, false);
        ShortStudentsDetailsAdaptor.ViewHolder vh = new ViewHolder(view);
        return vh;

    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        // set values for each item
        ShortStudentDetails itam = mData.get(position);
        viewHolder.StudentName.setText(itam.StudentName);
        viewHolder.Discreption.setText(itam.Discreption);
        Log.d("TAG", "onBindViewHolder: " + itam.PhootoURL);
        viewHolder.BranchPhoto.setImageResource(R.drawable.student_logo);

//        if(itam.PhootoURL.equals("NoImage"))
//        {
//           viewHolder.BranchPhoto.setImageResource(R.drawable.student_logo);
//
//        }
//        else
//        {
//            Picasso.get().load(itam.PhootoURL).into(viewHolder.BranchPhoto);
//        }


    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView StudentName , Discreption;
        public ImageView BranchPhoto;


        public ViewHolder(View v) {
            super(v);
            StudentName = (TextView) v.findViewById(R.id.StudentName_short_details);
            Discreption = (TextView) v.findViewById(R.id.branch_discreption_short_details);
            BranchPhoto = (ImageView) v.findViewById(R.id.student_image_short_details);
        }
    }
}
