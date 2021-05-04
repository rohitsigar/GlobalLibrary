package com.example.globallibrary.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.globallibrary.Activity.QuizActivity;
import com.example.globallibrary.Activity.Result;
import com.example.globallibrary.Adpaters.QuizReportAdaptor;
import com.example.globallibrary.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class QuizReportFragment extends Fragment {


    private static final String KEY_STUDENT_ID = "id_student";
    private static final String KEY_STUDENT_B_ID = "id_branch";
    private static final String SHARED_PREF = "PREF";
    FirebaseFirestore firebaseFirestore ;

    ImageButton BackPress;

    SharedPreferences sharedPreferences;

    String BranchId;
    String StudentId;


    int totolQuestion;
    int score;
    Button Score;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter _mAdapter;
    public RecyclerView.LayoutManager _layoutManager;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_quiz_report, container, false);

        totolQuestion = getArguments().getInt("Ques_total");
        score = getArguments().getInt("ans");
        Score = view.findViewById(R.id.score);
        Score.setText("Score : "  + String.valueOf(score) + " Out of " + String.valueOf(totolQuestion));
        recyclerView = (RecyclerView) view.findViewById(R.id.answers);
        RecyclerView.LayoutManager _layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setFocusable(false);
        BackPress = getActivity().findViewById(R.id.return_to_quiz);
        BackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });



        ArrayList<Result> list = new ArrayList();
        for(int i=0;i<totolQuestion;i++)
        {
            list.add(QuizActivity.getData(i));
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(_layoutManager);
        ///  add items to the adapter
        _mAdapter = new QuizReportAdaptor(list);
        ///  set Adapter to RecyclerView
        recyclerView.setAdapter(_mAdapter);


        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences  = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

        StudentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
        BranchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);


        java.util.Date currentTime = Calendar.getInstance().getTime();
        String time  = currentTime.toString().trim();
        String day = time.substring(0,3);
        String date =  time.substring(8,10) +" "+ time.substring(4,7) +" " + time.substring(30,34) ;
        String CurrentT = time.substring(11,19);
        Result result = QuizActivity.getData(0);
        firebaseFirestore  = FirebaseFirestore.getInstance();
        Map<String,Object> QuizReportStudent= new HashMap<>();
        Map<String,Object> QuizReportBranch= new HashMap<>();
       QuizReportBranch.put("StudentId" , StudentId);
       QuizReportBranch.put("Performance" , "Score : "  + String.valueOf(score) + " Out of " + String.valueOf(totolQuestion));
       QuizReportBranch.put("Catigory" , result.getCategory());
       QuizReportBranch.put("Difficulty" , result.getDifficulty());
       QuizReportBranch.put("Day" , day);
       QuizReportBranch.put("Date" , date);
       QuizReportBranch.put("Time"  , CurrentT);
        QuizReportBranch.put("Sortthis"  , currentTime);

        QuizReportStudent.put("Performance" , "Score : "  + String.valueOf(score) + " Out of " + String.valueOf(totolQuestion));
        QuizReportStudent.put("Catigory" , result.getCategory());
        QuizReportStudent.put("Difficulty" , result.getDifficulty());
        QuizReportStudent.put("Day" , day);
        QuizReportStudent.put("Date" , date);
        QuizReportStudent.put("Time"  , CurrentT);
        QuizReportStudent.put("Sortthis"  , currentTime);
        String Id = firebaseFirestore.collection("Branches").document(BranchId).collection("QuizReport").document().getId();
        firebaseFirestore.collection("Branches").document(BranchId).collection("QuizReport").document(Id).set(QuizReportBranch);
        String Id1 = firebaseFirestore.collection("Branches").document(BranchId).collection("StudentDetails").document(StudentId).collection("QuizReport").document().getId();
        firebaseFirestore.collection("Branches").document(BranchId).collection("StudentDetails").document(StudentId).collection("QuizReport").document(Id1).set(QuizReportStudent);
        

    }
}