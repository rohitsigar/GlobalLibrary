package com.example.globallibrary.Adpaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Models.NotificationDetails;
import com.example.globallibrary.R;

import java.util.List;
public class NotificationAdaptor  extends RecyclerView.Adapter<NotificationAdaptor.ViewHolder> {
    private LayoutInflater mInflater;
    private List<NotificationDetails> mData;


    public NotificationAdaptor(List<NotificationDetails> Data) {
        mData = Data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.notification_details, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        // set values for each item
        NotificationDetails itam = mData.get(position);
        viewHolder.Title.setText(itam.Title);
        viewHolder.Discreption.setText(itam.Discreption);
        viewHolder.Time.setText(String.valueOf(itam.Time));
        viewHolder.Day.setText(itam.Day);
        viewHolder.Date.setText(itam.Date);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView Title , Discreption , Day , Date, Time ;



        public ViewHolder(View v) {
            super(v);
            Title = (TextView) v.findViewById(R.id.Title_notification);
            Discreption = (TextView) v.findViewById(R.id.discreption_notification);
            Day = (TextView) v.findViewById(R.id.day_notification);
            Time = (TextView) v.findViewById(R.id.time_notification);
            Date = (TextView) v.findViewById(R.id.date_notification);

        }
    }
}