package com.example.globallibrary.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.globallibrary.Fragment.QuizItemFragment;
import com.example.globallibrary.Models.ReviewQuiz;
import com.example.globallibrary.R;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class QuizActivity extends AppCompatActivity {

    private static com.example.globallibrary.Activity.QuizItem item;
    String URL = "";
    int ans =0;

    ImageButton BackPress;

    private static ArrayList<ReviewQuiz> list;

    GifImageView progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color, getApplicationContext().getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.main_color));
        }

        setContentView(R.layout.activity_quiz);
        Intent intent = getIntent();
        URL = intent.getStringExtra("URL");
        progressBar = findViewById(R.id.progress_bar_9);
        BackPress = findViewById(R.id.return_to_quiz);
        BackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list = new ArrayList();
        progressBar.setVisibility(View.VISIBLE);
// ...

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("TAG", "onResponse: String : " + response);
                        GsonBuilder gsonBuilder  = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        item =  gson.fromJson(response , com.example.globallibrary.Activity.QuizItem.class);
                        Log.d("TAG", "onResponse: check " +item.getResponseCode() );
                        List<Result> results = item.getResults();
//                        Catigory.setText(results.get(0).getCategory());
//                        Difficuly.setText(results.get(0).getDifficulty());
                        Fragment fragment = new QuizItemFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("Ques_no" , 1);
                        bundle.putInt("Ques_total" , results.size());
                        bundle.putInt("ans" , 0);
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.quiz_questions, fragment , "playing").commit();
                        progressBar.setVisibility(View.GONE);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onResponse: String : " + "error");

            }
        });
        queue.add(stringRequest);












    }
    public static Result getData(int i)
    {
        List<Result> results = item.getResults();
        return results.get(i);
    }


    public static void setData(ReviewQuiz reviewQuiz)
    {
        list.add(reviewQuiz);
    }
    public static List<ReviewQuiz> sendData()
    {
        return  list;
    }


    @Override
    public void onBackPressed() {
        Fragment myFragment = getSupportFragmentManager().findFragmentByTag("playing");
        if (myFragment != null && myFragment.isVisible() && ans==0) {


            ViewGroup viewGroup = findViewById(android.R.id.content);

            //then we will inflate the custom alert dialog xml that we created
            View dialogView = LayoutInflater.from(QuizActivity.this).inflate(R.layout.quiz_quiz, viewGroup, false);


            //Now we need an AlertDialog.Builder object
            AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);

            //setting the view of the builder to our custom view that we already inflated
            builder.setView(dialogView);

            //finally creating the alert dialog and displaying it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            MaterialButton Quit = alertDialog.findViewById(R.id.quit);
            Quit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ans = 1;
                    onBackPressed();
                }
            });


            // add your code here
        }
        else
        {
            super.onBackPressed();
        }


    }
}