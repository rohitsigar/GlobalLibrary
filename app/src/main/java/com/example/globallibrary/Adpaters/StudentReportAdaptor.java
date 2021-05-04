package com.example.globallibrary.Adpaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Models.ReportQuizStudentItem;
import com.example.globallibrary.R;

import java.util.List;
public class StudentReportAdaptor  extends RecyclerView.Adapter<StudentReportAdaptor.ViewHolder> {
    private LayoutInflater mInflater;
    private List<ReportQuizStudentItem> mData;


    public StudentReportAdaptor(List<ReportQuizStudentItem> Data) {
        mData = Data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.quiz_item_report_student, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        // set values for each item
        ReportQuizStudentItem itam = mData.get(position);

        viewHolder.Difficulty.setText(itam.Difficulty);
        viewHolder.Time.setText(String.valueOf(itam.Time));
        viewHolder.Day.setText(itam.Day);
        viewHolder.Date.setText(itam.Date);
        viewHolder.Performance.setText(itam.Performance);
        viewHolder.Catigory.setText(itam.Catigory);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Catigory , Difficulty , Day , Date , Time , Performance ;




        public ViewHolder(View v) {
            super(v);

            Catigory = (TextView) v.findViewById(R.id.Catigory_report_student);
            Day = (TextView) v.findViewById(R.id.day_report_student);
            Time = (TextView) v.findViewById(R.id.time_report_student);
            Performance = v.findViewById(R.id.performance_report_student);
            Date = (TextView) v.findViewById(R.id.date_report_student);
            Difficulty = v.findViewById(R.id.difficuly_report_student);


        }
    }
}