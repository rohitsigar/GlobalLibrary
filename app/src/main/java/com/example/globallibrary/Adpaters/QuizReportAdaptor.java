package com.example.globallibrary.Adpaters;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Models.ReviewQuiz;
import com.example.globallibrary.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class QuizReportAdaptor  extends RecyclerView.Adapter<QuizReportAdaptor.ViewHolder> {
    private LayoutInflater mInflater;
    private List<ReviewQuiz> mData;


    public QuizReportAdaptor(List<ReviewQuiz> Data) {
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
        ReviewQuiz itam = mData.get(position);
        viewHolder.Question.setText(itam.getQuestion());
        viewHolder.Opt[0].setText(itam.getOpt1());

        viewHolder.Opt[1].setText(itam.getOpt2());

        viewHolder.Opt[2].setText(itam.getOpt3());

        viewHolder.Opt[3].setText(itam.getOpt4());

//        viewHolder.Opt[0].setBackgroundColor(Color.parseColor("#f4f4f4"));
//        viewHolder.Opt[0].setTextColor(Color.parseColor("#313335"));
//        viewHolder.Opt[1].setBackgroundColor(Color.parseColor("#f4f4f4"));
//        viewHolder.Opt[1].setTextColor(Color.parseColor("#313335"));
//        viewHolder.Opt[2].setBackgroundColor(Color.parseColor("#f4f4f4"));
//        viewHolder.Opt[2].setTextColor(Color.parseColor("#313335"));
//        viewHolder.Opt[3].setBackgroundColor(Color.parseColor("#f4f4f4"));
//        viewHolder.Opt[3].setTextColor(Color.parseColor("#313335"));
        viewHolder.Direct.setVisibility(View.VISIBLE);
        viewHolder.Revert.setVisibility(View.GONE);


        viewHolder.right[0].setVisibility(View.GONE);
        viewHolder.right[1].setVisibility(View.GONE);
        viewHolder.right[2].setVisibility(View.GONE);
        viewHolder.right[3].setVisibility(View.GONE);

        viewHolder.wrong[0].setVisibility(View.GONE);
        viewHolder.wrong[1].setVisibility(View.GONE);
        viewHolder.wrong[2].setVisibility(View.GONE);
        viewHolder.wrong[3].setVisibility(View.GONE);

        viewHolder.layout[0].setStrokeColor(Color.parseColor("#F2F7FE"));
        viewHolder.layout[1].setStrokeColor(Color.parseColor("#F2F7FE"));
        viewHolder.layout[2].setStrokeColor(Color.parseColor("#F2F7FE"));
        viewHolder.layout[3].setStrokeColor(Color.parseColor("#F2F7FE"));

        Log.d("TAG", "onBindViewHolder: checking   " + itam.getRight() + " " + itam.getWrong());

//        viewHolder.Opt[itam.getWrong()].setBackgroundColor(Color.parseColor("#D83A56"));
//        viewHolder.Opt[itam.getWrong()].setTextColor(Color.WHITE);

        viewHolder.wrong[itam.getWrong()].setVisibility(View.VISIBLE);
        viewHolder.layout[itam.getWrong()].setStrokeColor(Color.parseColor("#FF9B9B"));

//        viewHolder.Opt[itam.getRight()].setBackgroundColor(Color.parseColor("#00AF91"));
//        viewHolder.Opt[itam.getRight()].setTextColor(Color.WHITE);

        viewHolder.right[itam.getRight()].setVisibility(View.VISIBLE);
        viewHolder.layout[itam.getRight()].setStrokeColor(Color.parseColor("#46E6BA"));


        viewHolder.QuestionNo.setText("Question " + String.valueOf(position + 1));

        if(mData.size()==(position+1))
        {
             viewHolder.Direct.setVisibility(View.GONE);
             viewHolder.Revert.setVisibility(View.VISIBLE);
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
        public TextView Question ;
        public TextView[] Opt;
        public  Button QuestionNo;
        boolean b = true;
        public  MaterialCardView[] layout;

        public ImageView[] right;
        public  ImageView[] wrong;

        public ImageView Direct;

        public ImageView Revert;





        public ViewHolder(View v) {
            super(v);
            Opt = new TextView[4];
            layout  = new MaterialCardView[4];
            right = new ImageView[4];
            wrong = new ImageView[4];

            Question = v.findViewById(R.id.ques);
            Opt[0]= v.findViewById(R.id.opt1);
            Opt[1] = v.findViewById(R.id.opt2);
            Opt[2]  = v.findViewById(R.id.opt3);
            Opt[3]  = v.findViewById(R.id.opt4);
            QuestionNo = v.findViewById(R.id.Question_no);

            right[0] = v.findViewById(R.id.right1);
            right[1] = v.findViewById(R.id.right2);
            right[2] = v.findViewById(R.id.right3);
            right[3] = v.findViewById(R.id.right4);

            wrong[0] = v.findViewById(R.id.wrong1);
            wrong[1] = v.findViewById(R.id.wrong2);
            wrong[2] = v.findViewById(R.id.wrong3);
            wrong[3] = v.findViewById(R.id.wrong4);

            layout[0] = v.findViewById(R.id.layout1);
            layout[1] = v.findViewById(R.id.layout2);
            layout[2] = v.findViewById(R.id.layout3);
            layout[3] = v.findViewById(R.id.layout4);
            Direct = v.findViewById(R.id.direct);

            Revert = v.findViewById(R.id.direct1);

        }
    }
}