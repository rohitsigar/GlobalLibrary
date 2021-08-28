package com.example.globallibrary.Fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.globallibrary.Activity.QuizActivity;
import com.example.globallibrary.Activity.QuizItem;
import com.example.globallibrary.Activity.Result;
import com.example.globallibrary.Models.ReviewQuiz;
import com.example.globallibrary.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Random;

public class QuizItemFragment extends Fragment {


    private static QuizItem[] item;

    int QuesNo;
    TextView Ques;
    RadioButton[] opt;
    TextView QuesNoShow;
    int totalQues;
    MaterialButton Next;
    RadioGroup Markedans;

    TextView Catigory;
    TextView Difficuly;
   TextView Progress;
    ImageButton BackPress;
    int ans;
    boolean check = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        QuesNo = getArguments().getInt("Ques_no");
        totalQues = getArguments().getInt("Ques_total");
        ans = getArguments().getInt("ans");
        Result results =  QuizActivity.getData(QuesNo-1);
        Markedans = view.findViewById(R.id.radioGroup);
        Progress = view.findViewById(R.id.question_progress);
        Next = view.findViewById(R.id.NextQuestion);
        String temp = "" + String.valueOf(QuesNo) + "/" + String.valueOf(totalQues);
        Progress.getLayoutParams().width = ((QuesNo*100)/totalQues)*10;
        Progress.requestLayout();
        QuesNoShow = view.findViewById(R.id.Question_no);
        QuesNoShow.setText(temp);



        Catigory = view.findViewById(R.id.Catigory1);
        Difficuly = view.findViewById(R.id.difficuly1);
     Catigory.setText(results.getCategory().toString());
     Difficuly.setText(results.getDifficulty().toString());
        BackPress = getActivity().findViewById(R.id.return_to_quiz);
        BackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        Ques = view.findViewById(R.id.ques);
        opt = new RadioButton[4];
        opt[0] = view.findViewById(R.id.radio_button_1);
        opt[1]= view.findViewById(R.id.radio_button_2);
        opt[2] = view.findViewById(R.id.radio_button_3);
        opt[3] = view.findViewById(R.id.radio_button_4);
        Ques.setText(Html.fromHtml(results.getQuestion()));
        List<String> s1 = results.getIncorrectAnswers();
        Random random = new Random();
        int x = random.nextInt(4);
        int j=0;
        for(int i=0;i<4;i++)
        {
            if(i==x)
            {
                opt[i].setText(Html.fromHtml(results.getCorrectAnswer()));
            }
            else
            {
                opt[i].setText(Html.fromHtml(s1.get(j)));
                j++;
            }
        }
        if(totalQues==QuesNo)
        {
            Next.setText("Finish");
        }

        ReviewQuiz reviewQuiz = new ReviewQuiz();


        reviewQuiz.setOpt1(opt[0].getText().toString());
        reviewQuiz.setOpt2(opt[1].getText().toString());
        reviewQuiz.setOpt3(opt[2].getText().toString());
        reviewQuiz.setOpt4(opt[3].getText().toString());
        reviewQuiz.setQuestion(Ques.getText().toString());

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               for(int i=0;i<4;i++)
               {
                   if(opt[i].isChecked()  && !check)
                   {

                       check = true;
                       reviewQuiz.setRight(x);
                       reviewQuiz.setWrong(i);

                       QuizActivity.setData(reviewQuiz);

                       if(i==x)
                       {
                           Fragment fragment;
                           if(totalQues==QuesNo)
                           {
                                fragment = new QuizReportFragment();
                           }
                           else
                           {
                                fragment = new QuizItemFragment();
                           }
                           Bundle bundle = new Bundle();
                           bundle.putInt("Ques_no" , QuesNo+1);
                           bundle.putInt("Ques_total" , totalQues);
                           bundle.putInt("ans" , ans+1);
                           fragment.setArguments(bundle);
                           FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                           FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                           fragmentTransaction.replace(R.id.quiz_questions, fragment);
                           fragmentTransaction.commit();
                       }
                       else
                       {
                           Fragment fragment;
                           if(totalQues==QuesNo)
                           {
                               fragment = new QuizReportFragment();
                           }
                           else
                           {
                               fragment = new QuizItemFragment();
                           }
                           Bundle bundle = new Bundle();
                           bundle.putInt("Ques_no" , QuesNo+1);
                           bundle.putInt("Ques_total" , totalQues);
                           bundle.putInt("ans" , ans);
                           fragment.setArguments(bundle);
                           FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                           FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                           fragmentTransaction.replace(R.id.quiz_questions, fragment);
                           fragmentTransaction.commit();
                       }
                   }
               }

            }
        });







    }
}