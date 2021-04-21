
package com.example.globallibrary.Adpaters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Models.ShortStudentDetails;
import com.example.globallibrary.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShortStudentsDetailsAdaptor extends RecyclerView.Adapter<ShortStudentsDetailsAdaptor.ViewHolder> {
    private LayoutInflater mInflater;
    private List<ShortStudentDetails> mData;
    RecyclerViewClickListner listner;

    FirebaseStorage storage;
    StorageReference storageReference;


    public ShortStudentsDetailsAdaptor(List<ShortStudentDetails> Data , RecyclerViewClickListner listner) {
       this.mData = Data;
        this.listner  = listner;

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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        // set values for each item
        ShortStudentDetails itam = mData.get(position);
        viewHolder.StudentName.setText(itam.StudentName);
        viewHolder.Discreption.setText(itam.Discreption);
        if(itam.Color.equals("Red"))
        {
           viewHolder.ChangeColor.setBackgroundColor(Color.RED);
        }
        else if(itam.Color.equals("Green"))
        {
            viewHolder.ChangeColor.setBackgroundColor(Color.GREEN);
        }
        Log.d("TAG", "onBindViewHolder: " + itam.PhootoURL);

        if (itam.PhootoURL.equals("NoImage")) {
            viewHolder.StudentPhoto.setImageResource(R.drawable.student_logo);

        } else {
            Picasso.get().load(itam.PhootoURL).into(viewHolder.StudentPhoto);
        }


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface RecyclerViewClickListner {
        void onClick(View v, int position);

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView StudentName, Discreption ;
        public ImageView StudentPhoto;
        public LinearLayout ChangeColor;


        public ViewHolder(final View v) {
            super(v);
            StudentName = (TextView) v.findViewById(R.id.StudentName_short_details);
            Discreption = (TextView) v.findViewById(R.id.branch_discreption_short_details);
            StudentPhoto = (ImageView) v.findViewById(R.id.student_image_short_details);
            ChangeColor = (LinearLayout) v.findViewById(R.id.ChangeColor);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listner.onClick(v, getAdapterPosition());

        }
    }
}
