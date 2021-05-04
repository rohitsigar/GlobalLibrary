package com.example.globallibrary.Adpaters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Activity.Result;
import com.example.globallibrary.R;

import java.util.List;
public class QuizReportAdaptor  extends RecyclerView.Adapter<QuizReportAdaptor.ViewHolder> {
    private LayoutInflater mInflater;
    private List<Result> mData;


    public QuizReportAdaptor(List<Result> Data) {
        mData = Data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.question_ans_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        // set values for each item
        Result itam = mData.get(position);
        viewHolder.Answer.setText(Html.fromHtml(itam.getCorrectAnswer()));
        viewHolder.Question.setText(Html.fromHtml(itam.getQuestion()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button Question , Answer;



        public ViewHolder(View v) {
            super(v);
            Question = v.findViewById(R.id.Question);
            Answer = v.findViewById(R.id.answer);

        }
    }
}