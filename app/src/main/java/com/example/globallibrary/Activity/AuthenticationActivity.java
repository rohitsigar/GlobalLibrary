package com.example.globallibrary.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.globallibrary.LoginBranchOwner;
import com.example.globallibrary.R;
import com.example.globallibrary.fragments.JoinLogin;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.authFrame,new JoinLogin()).addToBackStack("Login").commit();
    }
}