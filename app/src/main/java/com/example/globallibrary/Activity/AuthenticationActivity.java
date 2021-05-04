package com.example.globallibrary.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.globallibrary.Fragment.MainPageAuthFragment;
import com.example.globallibrary.R;


public class AuthenticationActivity<ViewPagerAdapter> extends FragmentActivity {


//    private static final int PERMISSION_CODE_EXTERAL = 1;
//    private static final int PERMISSION_CODE_INTERNET = 1;
    private static final int   MY_PERMISSIONS_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        checkPermission();



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_authentication);
        Fragment fragment = new MainPageAuthFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_auth,fragment).addToBackStack(null).commit();
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
        AuthenticationActivity.this.finish();
        System.exit(0);
    }
}