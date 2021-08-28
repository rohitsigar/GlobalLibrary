package com.example.globallibrary.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.globallibrary.Activity.QuizActivity;
import com.example.globallibrary.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizStudentFragment extends Fragment {
    TextInputEditText Questions;
    AutoCompleteTextView Cat;
    AutoCompleteTextView Diff;
    Map<String,String> Map1;
    MaterialButton Start;
    String URL = "";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Cat = view.findViewById(R.id.catigory);
        Start = view.findViewById(R.id.shart_quiz);
        Diff = view.findViewById(R.id.difficuly);
        Questions = view.findViewById(R.id.Questions);
        List<String> Catigory = new ArrayList<>();
        Catigory.add("General Knowledge");
        Catigory.add("Entertainment: Books");
        Catigory.add("Entertainment: Film");
        Catigory.add("Entertainment: Music");
        Catigory.add("Entertainment: Musicals and Theatres");
        Catigory.add("Entertainment: Television");
        Catigory.add("Entertainment: Video Games");
        Catigory.add("Entertainment: Board Games");
        Catigory.add("Science and Nature");
        Catigory.add("Science: Computers");
        Catigory.add("Science: Mathematics");
        Catigory.add("Mythology");
        Catigory.add("History");
        Catigory.add("Sports");
        Catigory.add("Geography");
        Catigory.add("Politics");
        Catigory.add("Art");
        Catigory.add("Celebrities");
        Catigory.add("Animals");
        Catigory.add("Vehicles");
        Catigory.add("Entertainment: Comics");
        Catigory.add("Science: Gadgets");
        Catigory.add("Entertainment: Japanese Anime and Manga");
        Catigory.add("Entertainment: Cartoon and Animations");
        List<String> Difficulty = new ArrayList<>();
        Difficulty.add("easy");
        Difficulty.add("medium");
        Difficulty.add("hard");
        ArrayAdapter<String> adaptor = new ArrayAdapter<String>(getContext(), R.layout.drop_down_item_branch_name_with_address,Catigory);
        Cat.setAdapter(adaptor);
        Cat.setDropDownBackgroundDrawable(ContextCompat.getDrawable(getContext() , R.drawable.newbar_background));
        Cat.setDropDownHeight(1000);
        ArrayAdapter<String> adaptor1 = new ArrayAdapter<String>(getContext(), R.layout.drop_down_item_branch_name_with_address,Difficulty);
        Diff.setAdapter(adaptor1);
        Diff.setDropDownBackgroundDrawable(ContextCompat.getDrawable(getContext() , R.drawable.newbar_background));
        Diff.setDropDownHeight(300);
        Map1 = new HashMap<>();
        Map1.put("General Knowledge" , "9");
        Map1.put("Entertainment: Books" , "10");
        Map1.put("Entertainment: Film" , "11");
        Map1.put("Entertainment: Music" , "12");
        Map1.put("Entertainment: Musicals and Theatres" , "13");
        Map1.put("Entertainment: Television" , "14");
        Map1.put("Entertainment: Video Games" , "15");
        Map1.put("Entertainment: Board Games" , "16");
        Map1.put("Science and Nature" , "17");
        Map1.put("Science: Computers" , "18");
        Map1.put("Science: Mathematics" , "19");
        Map1.put("Mythology" , "20");
        Map1.put("Sports"  ,"21");
        Map1.put("Geography"  ,"22");
        Map1.put("History" , "23");
        Map1.put("Politics" , "24");
        Map1.put("Art" , "25");
        Map1.put("Celebrities" , "26");
        Map1.put("Animals" , "27");
        Map1.put("Vehicles",  "28");
        Map1.put("Entertainment: Comics","29");
        Map1.put("Science: Gadgets"  ,"30");
        Map1.put("Entertainment: Japanese Anime and Manga" ,"31");
        Map1.put("Entertainment: Cartoon and Animations" ,"32");
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Cati = Cat.getText().toString().trim();
                String Dif = Diff.getText().toString().trim();
                if(Questions.getText().toString().isEmpty())
                {
                    Questions.setError("Enter Number of Questions");
                }
               else if(Cat.getText().toString().isEmpty())
                {
                    Cat.setError("This Filed is Empty");
                }
               else if(Diff.getText().toString().trim().isEmpty())
                {
                    Diff.setError("This Filed is Empty");
                }
               else
                {
                    int ques = Integer.parseInt(Questions.getText().toString().trim());
                     if(ques > 50)
                {
                    Questions.setError("Maximum Number of Question Can be 50");
                }
                     else
                     {
                         URL = "https://opentdb.com/api.php?amount=" + ques + "&category=" + Map1.get(Cati) +"&difficulty=" + Dif + "&type=multiple" ;
                         Log.d("TAG", "onClick: Url" + URL);
                         Intent intent = new Intent(getActivity() , QuizActivity.class );
                         intent.putExtra("URL" , URL);
                         startActivity(intent);
                     }



                }


            }
        });















    }
}