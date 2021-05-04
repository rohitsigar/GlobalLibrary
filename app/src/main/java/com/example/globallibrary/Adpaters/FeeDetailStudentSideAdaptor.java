
package com.example.globallibrary.Adpaters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Models.FeeDetailsStudentSide;
import com.example.globallibrary.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class FeeDetailStudentSideAdaptor extends RecyclerView.Adapter<FeeDetailStudentSideAdaptor.ViewHolder> {
    private LayoutInflater mInflater;
    private List<FeeDetailsStudentSide> mData;
    RecyclerViewClickListner listner;

    FirebaseStorage storage;
    StorageReference storageReference;


    public FeeDetailStudentSideAdaptor(List<FeeDetailsStudentSide> Data , RecyclerViewClickListner listner) {
        this.mData = Data;
        this.listner  = listner;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fee_item_student, viewGroup, false);


        FeeDetailStudentSideAdaptor.ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        // set values for each item
        FeeDetailsStudentSide itam = mData.get(position);
        viewHolder.Amount.setText(String.valueOf(itam.Amount));
        viewHolder.DueDate.setText(itam.DueDate);
        if(itam.Status)
        {
            viewHolder.Pay.setVisibility(View.GONE);
            viewHolder.Status.setCardBackgroundColor(Color.parseColor(	"#90EE90"));
        }
        else
        {
            viewHolder.Pay.setVisibility(View.VISIBLE);
            viewHolder.Status.setCardBackgroundColor(Color.parseColor(	"#ffcccb"));
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
        public TextView Amount, DueDate ;
        public MaterialButton Pay;
        public CardView Status;


        public ViewHolder(final View v) {
            super(v);
            Amount = (TextView) v.findViewById(R.id.amount_student_side);
            DueDate = (TextView) v.findViewById(R.id.due_date_student_side);
            Status = v.findViewById(R.id.status_student_side);
            Pay = v.findViewById(R.id.pay_fees_student_side);

            Pay.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            listner.onClick(v, getAdapterPosition());

        }
    }
}
