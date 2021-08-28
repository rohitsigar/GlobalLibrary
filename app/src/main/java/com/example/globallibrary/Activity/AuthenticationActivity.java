package com.example.globallibrary.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.globallibrary.Fragment.BranchOwnerLoginFragment;
import com.example.globallibrary.Fragment.FragmentAllBranches;
import com.example.globallibrary.Fragment.LoginStudentFragment;
import com.example.globallibrary.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


public class AuthenticationActivity extends AppCompatActivity {



    ChipNavigationBar AuthMenu;

    ImageButton menu;

    RelativeLayout AuthLayout;
    LinearLayout ToolbarLayout;
    private static final int   MY_PERMISSIONS_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
        }

        setContentView(R.layout.activity_authentication);
        checkPermission();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_auth, new LoginStudentFragment() , "StudentFragment").commit();

        AuthMenu = findViewById(R.id.auth_navbar);

        AuthMenu.setItemSelected(R.id.auth_student, true);

        menu = findViewById(R.id.auth_menu);

        ToolbarLayout = findViewById(R.id.toolbar_layout);

        AuthLayout = findViewById(R.id.auth_background);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(AuthMenu.getVisibility()==View.VISIBLE)
                {
                    AuthMenu.setVisibility(View.GONE);
                }
                else
                {
                    AuthMenu.setVisibility(View.VISIBLE);
                }

            }
        });

        AuthMenu.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i)
                {
                    case R.id.auth_student :
                        fragment = new LoginStudentFragment();
                        ToolbarLayout.setBackgroundColor(getColor(R.color.white));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            getWindow().setStatusBarColor(getResources().getColor(R.color.white, getApplicationContext().getTheme()));
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                        }

                        AuthLayout.setBackgroundColor(getColor(R.color.white));
                        break;
                    case R.id.auth_branch :
                        fragment  = new BranchOwnerLoginFragment();
                        ToolbarLayout.setBackgroundColor(getColor(R.color.white));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            getWindow().setStatusBarColor(getResources().getColor(R.color.white, getApplicationContext().getTheme()));
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                        }

                        AuthLayout.setBackgroundColor(getColor(R.color.white));
                        break;
                    case R.id.auth_all_branches:

                        fragment = new FragmentAllBranches();
                        ToolbarLayout.setBackgroundColor(getColor(R.color.main_color));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color, getApplicationContext().getTheme()));
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.main_color));
                        }

                        AuthLayout.setBackgroundColor(getColor(R.color.main_color));
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_auth, fragment).commit();

                AuthMenu.setVisibility(View.GONE);
            }
        });


//        Fragment fragment = new MainPageAuthFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_auth,fragment).addToBackStack(null).commit();
    }
    protected void checkPermission(){
        if(ContextCompat.checkSelfPermission(AuthenticationActivity.this,Manifest.permission.INTERNET)
                + ContextCompat.checkSelfPermission(
                AuthenticationActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(
                AuthenticationActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                +ContextCompat.checkSelfPermission(
                AuthenticationActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)
                + ContextCompat.checkSelfPermission(
                AuthenticationActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            // Do something, when permissions not granted
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    AuthenticationActivity.this,Manifest.permission.INTERNET)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    AuthenticationActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    AuthenticationActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ||ActivityCompat.shouldShowRequestPermissionRationale(
                    AuthenticationActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)
                    ||ActivityCompat.shouldShowRequestPermissionRationale(
                    AuthenticationActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)){
                // If we should give explanation of requested permissions

                // Show an alert dialog here with request explanation
                AlertDialog.Builder builder = new AlertDialog.Builder(AuthenticationActivity.this);
                builder.setMessage("Camera, Read Contacts and Write External" +
                        " Storage permissions are required to do the task.");
                builder.setTitle("Please grant those permissions");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                AuthenticationActivity.this,
                                new String[]{
                                        Manifest.permission.INTERNET ,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                },
                                MY_PERMISSIONS_REQUEST_CODE
                        );
                    }
                });
                builder.setNeutralButton("Cancel",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                // Directly request for required permissions, without explanation
                ActivityCompat.requestPermissions(
                        AuthenticationActivity.this,
                        new String[]{
                                Manifest.permission.INTERNET,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE ,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );
            }
        }else {
            // Do something, when permissions are already granted
            Toast.makeText(AuthenticationActivity.this,"Permissions already granted",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CODE:{
                // When request is cancelled, the results array are empty
                if(
                        (grantResults.length >0) &&
                                (grantResults[0]
                                        + grantResults[1]
                                        + grantResults[2]
                                        +grantResults[3]
                                        +grantResults[4]
                                        == PackageManager.PERMISSION_GRANTED
                                )
                ){
                    // Permissions are granted
                    Toast.makeText(AuthenticationActivity.this,"Permissions granted.",Toast.LENGTH_SHORT).show();
                }else {
                    // Permissions are denied
                    Toast.makeText(AuthenticationActivity.this,"Permissions denied.",Toast.LENGTH_SHORT).show();
                    AuthenticationActivity.this.finish();
                    System.exit(0);
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {

        Fragment myFragment = getSupportFragmentManager().findFragmentByTag("StudentFragment");
        if (myFragment != null && myFragment.isVisible()) {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
            // add your code here
        }
        else
        {  AuthMenu.setItemSelected(R.id.auth_student, true);
                Fragment fragment = new LoginStudentFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_auth, fragment , "StudentFragment").commit();
            }
        }
}