package com.example.globallibrary.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.globallibrary.Fragment.SettingFragmentBranch;
import com.example.globallibrary.Fragment.StudentSettingFragment;
import com.example.globallibrary.R;

public class Settings extends AppCompatActivity {

    String BranchId;
    String StudentId;
    ImageButton BackPress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent intent = getIntent();
        BranchId = intent.getStringExtra("BranchId");
        StudentId = intent.getStringExtra("StudentId");
        Log.d("TAG", "onCreate: StudentId " + StudentId);
        findViewById(R.id.return_back111_backup).setVisibility(View.GONE);
        findViewById(R.id.return_back111).setVisibility(View.VISIBLE);
        BackPress = findViewById(R.id.return_back111);
        BackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Fragment myFragment = getSupportFragmentManager().findFragmentByTag("SettingFragment");
        if (myFragment != null && myFragment.isVisible()) {
            finish();
            // add your code here
        }


            }
        });

        if(StudentId !=null)
        {
            Fragment fragment = new StudentSettingFragment();
            Bundle bundle = new Bundle();
            bundle.putString("StudentId" , StudentId);
            bundle.putString("BranchId" , BranchId);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_setting,fragment , "SettingFragment").commit();
        }
        else
        {
            Fragment fragment = new SettingFragmentBranch();
            Bundle bundle = new Bundle();
            bundle.putString("BranchId" , BranchId);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_setting,fragment , "SettingFragment").commit();
        }


    }

    @Override
    public void onBackPressed() {
//        Fragment myFragment = getSupportFragmentManager().findFragmentByTag("SettingFragment");
//        if (myFragment != null && myFragment.isVisible()) {
//            Intent intent = new Intent(Settings.this , GeneralActivity.class);
//            startActivity(intent);
//            // add your code here
//        }
//        else
//        {
            super.onBackPressed();
//        }

    }
}