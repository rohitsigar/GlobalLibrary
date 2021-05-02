package com.example.globallibrary.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        if(StudentId !=null)
        {
            Fragment fragment = new StudentSettingFragment();
            Bundle bundle = new Bundle();
            bundle.putString("StudentId" , StudentId);
            bundle.putString("BranchId" , BranchId);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_setting,fragment).addToBackStack(null).commit();
        }
        else
        {
            Fragment fragment = new SettingFragmentBranch();
            Bundle bundle = new Bundle();
            bundle.putString("BranchId" , BranchId);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_setting,fragment).addToBackStack(null).commit();
        }


    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(this ,GeneralActivity.class );
//        intent.putExtra("user","branchAccess");
//        intent.putExtra("branchId" , BranchId);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
        super.onBackPressed();
    }
}