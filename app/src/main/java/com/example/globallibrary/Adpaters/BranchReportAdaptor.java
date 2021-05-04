package com.example.globallibrary.Adpaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Models.ReportQuizBranchItem;
import com.example.globallibrary.R;
import com.squareup.picasso.Picasso;

import java.util.List;
public class BranchReportAdaptor  extends RecyclerView.Adapter<BranchReportAdaptor.ViewHolder> {
    private LayoutInflater mInflater;
    private List<ReportQuizBranchItem> mData;


    public BranchReportAdaptor(List<ReportQuizBranchItem> Data) {
        mData = Data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.quiz_report_branch_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        // set values for each item
        ReportQuizBranchItem itam = mData.get(position);
        viewHolder.StudentName.setText(itam.StudentName);
        viewHolder.Difficulty.setText(itam.Difficulty);
        viewHolder.Time.setText(String.valueOf(itam.Time));
        viewHolder.Day.setText(itam.Day);
        viewHolder.Date.setText(itam.Date);
        viewHolder.Performance.setText(itam.Performance);
        viewHolder.Catigory.setText(itam.Catigory);
        if(itam.PhotoURL=="NoImage")
        {
            //nothing
        }
        else
        {
            Picasso.get().load(itam.PhotoURL).into(viewHolder.StudentImage);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView StudentName , Catigory , Difficulty , Day , Date , Time , Performance ;
        ImageView StudentImage;



        public ViewHolder(View v) {
            super(v);
            StudentName = (TextView) v.findViewById(R.id.student_name_report_branch);
            Catigory = (TextView) v.findViewById(R.id.Catigory_report_branch);
            Day = (TextView) v.findViewById(R.id.day_report_branch);
            Time = (TextView) v.findViewById(R.id.time_report_branch);
            Performance = v.findViewById(R.id.performance_report_branch);
            Date = (TextView) v.findViewById(R.id.date_report_branch);
            Difficulty = v.findViewById(R.id.difficuly_report_branch);
            StudentImage = v.findViewById(R.id.student_image_report_branch);

        }
    }
}