package com.example.globallibrary.Models;

import com.example.globallibrary.Activity.QuizItem;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class QuizApi {


    public static GetData getData = null;

    public static GetData getService()
    {
        if(getData==null)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            getData = retrofit.create(GetData.class);
        }
        return getData;
    }


    public interface GetData
    {

        @GET("https://opentdb.com/api.php?amount=2&category=12&difficulty=easy&type=multiple")
        Call<QuizItem> getQuestion();
    }

}
