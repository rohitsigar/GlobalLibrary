package com.example.globallibrary.Activity;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.globallibrary.Fragment.MainPageAuthFragment;
import com.example.globallibrary.R;


public class AuthenticationActivity<ViewPagerAdapter> extends FragmentActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_authentication);
        Fragment fragment = new MainPageAuthFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_auth,fragment).addToBackStack(null).commit();
    }

}