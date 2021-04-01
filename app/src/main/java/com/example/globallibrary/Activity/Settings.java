package com.example.globallibrary.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.globallibrary.Fragment.SettingFragment;
import com.example.globallibrary.R;

public class Settings extends AppCompatActivity {

    String BranchName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent intent = getIntent();
        BranchName = intent.getStringExtra("branchName");

        Fragment fragment = new SettingFragment();
        Bundle bundle = new Bundle();
        bundle.putString("branchName" , BranchName);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_setting,fragment).commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this ,GeneralActivity.class );
        intent.putExtra("user","branchAccess");
        intent.putExtra("branchName" , BranchName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        super.onBackPressed();
    }
}