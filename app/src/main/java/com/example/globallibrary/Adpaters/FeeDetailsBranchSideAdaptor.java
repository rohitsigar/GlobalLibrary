
package com.example.globallibrary.Adpaters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Models.FeeDetailsBranchSide;
import com.example.globallibrary.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FeeDetailsBranchSideAdaptor extends RecyclerView.Adapter<FeeDetailsBranchSideAdaptor.ViewHolder> {
    private LayoutInflater mInflater;
    private List<FeeDetailsBranchSide> mData;
    RecyclerViewClickListner listner;

    FirebaseStorage storage;
    StorageReference storageReference;


    public FeeDetailsBranchSideAdaptor(List<FeeDetailsBranchSide> Data , RecyclerViewClickListner listner) {
        this.mData = Data;
        this.listner  = listner;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fee_item_branch, viewGroup, false);


        FeeDetailsBranchSideAdaptor.ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        // set values for each item
        FeeDetailsBranchSide itam = mData.get(position);
        viewHolder.Amount.setText(String.valueOf(itam.Amount));
        viewHolder.DueDate.setText(itam.DueDate);
        viewHolder.StudentName.setText(itam.StudentName);
        if(itam.Status)
        {
            viewHolder.Mark.setVisibility(View.GONE);
            viewHolder.Paid.setVisibility(View.VISIBLE);

        }
        else
        {
            viewHolder.Mark.setVisibility(View.VISIBLE);
            viewHolder.Paid.setVisibility(View.GONE);

        }
        if(itam.URL.equals("NOImage"))
        {
            //nothing
        }
        else
        {
            Picasso.get().load(itam.URL).into(viewHolder.StudentImage);
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
        public TextView Amount, DueDate , StudentName ;
        public TextView Mark;

        public ImageView StudentImage;
        public  TextView Paid;



        public ViewHolder(final View v) {
            super(v);
            Amount = (TextView) v.findViewById(R.id.amount_branch_side);
            StudentName = v.findViewById(R.id.student_name_branch_side);
            DueDate = (TextView) v.findViewById(R.id.due_date_branch_side);
            Mark = (TextView) v.findViewById(R.id.pay_fees_branch_side);
            StudentImage = v.findViewById(R.id.student_image_branch_side);

            Paid =(TextView) v.findViewById(R.id.paid_branch_side);


            Mark.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listner.onClick(v, getAdapterPosition());
        }
    }
}
