package com.example.globallibrary.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.globallibrary.Fragment.QuizItemFragment;
import com.example.globallibrary.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private static com.example.globallibrary.Activity.QuizItem item;
    String URL = "";
    TextView Catigory;
    TextView Difficuly;
    int ans =0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Catigory = findViewById(R.id.Catigory1);
        Difficuly = findViewById(R.id.difficuly1);
        Intent intent = getIntent();
        URL = intent.getStringExtra("URL");
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
                        Catigory.setText(results.get(0).getCategory());
                        Difficuly.setText(results.get(0).getDifficulty());
                        Fragment fragment = new QuizItemFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("Ques_no" , 1);
                        bundle.putInt("Ques_total" , results.size());
                        bundle.putInt("ans" , 0);

                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.quiz_questions, fragment).addToBackStack(null).commit();

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

}