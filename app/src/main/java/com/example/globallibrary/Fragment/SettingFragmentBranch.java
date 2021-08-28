package com.example.globallibrary.Fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class SettingFragmentBranch extends Fragment   {

    TextView Passward;
    TextView ContactNumber;
    String BranchId;


    AlertDialog alertDialog;

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    TextView ChangeFee;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_branch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().findViewById(R.id.return_back111_backup).setVisibility(View.GONE);
        getActivity().findViewById(R.id.return_back111).setVisibility(View.VISIBLE);




        BranchId = getArguments().getString("BranchId");
        Passward = view.findViewById(R.id.change_passward);
        ContactNumber = view.findViewById(R.id.change_phone_no);


//        ChangeLocation = view.findViewById(R.id.change_location);
        ChangeFee = view.findViewById(R.id.change_default_amount);

     ChangeFee.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             ViewGroup viewGroup = getView().findViewById(android.R.id.content);

             //then we will inflate the custom alert dialog xml that we created
             View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.change_default_fee_dilog, viewGroup, false);


             //Now we need an AlertDialog.Builder object
             AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

             //setting the view of the builder to our custom view that we already inflated
             builder.setView(dialogView);

             //finally creating the alert dialog and displaying it
             alertDialog = builder.create();
             alertDialog.show();
             EditText FeeAmount = alertDialog.findViewById(R.id.amount_change);
             MaterialButton Change = alertDialog.findViewById(R.id.button_change);
             DocumentReference docIdRef = firebaseFirestore.collection("/Branches/" ).document(BranchId.trim());
             docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                 @Override
                 public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                     if (task.isSuccessful()) {
                         DocumentSnapshot document = task.getResult();
                         if (document.exists()) {

                            FeeAmount.setText(String.valueOf(document.getDouble("DefaultAmount")));
                         } else {

                             Log.d("TAG", "onComplete: not possible" );

                         }
                     } else {

                     }
                 }
             });

             Change.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if(FeeAmount.getText().toString().trim().isEmpty())
                     {
                         FeeAmount.setError("This Filed Can't be Empty");
                     }
                     else
                     {
                         firebaseFirestore.collection("Branches").document(BranchId).update("DefaultAmount" ,Double.parseDouble(FeeAmount.getText().toString()));
                         Toast.makeText(getActivity() , "Fee is Sucessfully Changed to " +FeeAmount.getText().toString()  , Toast.LENGTH_SHORT).show();
                     }

                 }
             });




         }
     });


//        DocumentReference docIdRef = firebaseFirestore.collection("/Branches/" ).document(BranchId.trim());
//        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//
//                        if(document.getGeoPoint("Location")!=null)
//                        {
//                            MarkLocation.setVisibility(View.GONE);
//                            ChangeLocation.setVisibility(View.VISIBLE);
//                        }
//                        else
//                        {
//                            MarkLocation.setVisibility(View.VISIBLE);
//                            ChangeLocation.setVisibility(View.GONE);
//                        }
//
//
//
//                    } else {
//
//                        Log.d("TAG", "onComplete: not possible" );
//
//                    }
//                } else {
//
//                }
//            }
//        });

//        ChangeLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ViewGroup viewGroup = getView().findViewById(android.R.id.content);
//
//                //then we will inflate the custom alert dialog xml that we created
//                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.add_location_branch_dilog, viewGroup, false);
//
//
//                //Now we need an AlertDialog.Builder object
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//                //setting the view of the builder to our custom view that we already inflated
//                builder.setView(dialogView);
//
//                //finally creating the alert dialog and displaying it
//                alertDialog = builder.create();
//                alertDialog.show();
//                Done = alertDialog.findViewById(R.id.buttonOk);
//                Radi = alertDialog.findViewById(R.id.radious);
//                 progressBar = alertDialog.findViewById(R.id.progressbar_location);
//                Done.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(Radi.getText().toString()==null)
//                        {
//                            Toast.makeText(getActivity(),"Please Enter Radious",Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                        {
//                            if(!checkPlayServiceInstalled()){
//                                return;
//                            }
//
//                            //now checking permission and request permission
//
//                            if(Build.VERSION.SDK_INT>=23){
//                                if(checkPermission()){
//                                    getDeviceLocation();
//                                }
//                                else{
//                                    requestPermission();
//                                }
//                            }
//                            else{
//                                getDeviceLocation();
//                            }
//
//
//                        }
//
//                    }
//                });
//            }
//        });





        Passward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ChangePasswardBranchFragment();
                Bundle bundle = new Bundle();
                bundle.putString("BranchId" , BranchId);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_setting, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        ContactNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ChangePhoneNumberBranchFragment();
                Bundle bundle = new Bundle();
                bundle.putString("BranchId" , BranchId);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_setting, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



    }
//    private void getDeviceLocation() {
//        locationManager=(LocationManager)getActivity().getSystemService(Service.LOCATION_SERVICE);
//        isGpsProvider=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        isNetworkProvider=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        if(!isGpsProvider && !isNetworkProvider){
//            //showing setting for enable gps
//            showSettingAlert();
//        }
//        else{
//            GetLocationData();
//        }
//    }

//    private void GetLocationData() {
//        googleApiClient=new GoogleApiClient.Builder(getActivity())
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//
//        googleApiClient.connect();
//    }
//    private void showSettingAlert() {
//        AlertDialog.Builder al=new AlertDialog.Builder(getActivity());
//        al.setTitle("Enable GPS");
//        al.setMessage("Please Enable GPS");
//        al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//
//            }
//        });
//        al.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        al.show();
//    }
//
//    private void requestPermission() {
//        ActivityCompat.requestPermissions(getActivity(),permissions_all,PERMISSION_CODE);
//    }
//
//    private boolean checkPermission() {
//        for(int i=0;i<permissions_all.length;i++){
//            int result= ContextCompat.checkSelfPermission(getActivity(),permissions_all[i]);
//            if(result== PackageManager.PERMISSION_GRANTED){
//                continue;
//            }
//            else {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case PERMISSION_CODE:
//                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    getDeviceLocation();
//                }
//                else{
//                    Toast.makeText(getActivity(), "Permission Failed", Toast.LENGTH_SHORT).show();
//                }
//        }
//    }
//    private boolean checkPlayServiceInstalled() {
//        GoogleApiAvailability apiAvailability=GoogleApiAvailability.getInstance();
//        int result=apiAvailability.isGooglePlayServicesAvailable(getActivity());
//        if(result!= ConnectionResult.SUCCESS){
//            if(apiAvailability.isUserResolvableError(result)){
//                apiAvailability.getErrorDialog(getActivity(),result,PLAY_SERVICE_REQUEST).show();
//                return false;
//            }
//            else{
//                return false;
//            }
//        }
//        else{
//            return true;
//        }
//    }

//    @SuppressWarnings(value = "MissingPermission")
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        location=LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//        GeoPoint geoPoint  = new GeoPoint(location.getLatitude() ,location.getLongitude());
//        firebaseFirestore.collection("Branches").document(BranchId).update("Location" , geoPoint);
//        firebaseFirestore.collection("Branches").document(BranchId).update("Radius" , Double.parseDouble(Radi.getText().toString().trim()));
//        progressBar.setVisibility(View.GONE);
//        alertDialog.dismiss();
//
//
//
//        if(location!=null){
//            Toast.makeText(getActivity(), "Lat : "+location.getLatitude()+" Lng "+location.getLongitude(), Toast.LENGTH_SHORT).show();
//            if(googleMap!=null){
//                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
//                googleMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10f));
//            }
//        }
//        startLocationUpdates();

//    }
//    @SuppressLint("MissingPermission")
//    private void startLocationUpdates() {
//        LocationRequest locationRequest=new LocationRequest();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        //10 sec
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(5000);
//        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this::onLocationChanged);
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//    private void stopLocationUpdates() {
//        if(googleApiClient!=null){
//            if(googleApiClient.isConnected()){
//                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this::onLocationChanged);
//                googleApiClient.disconnect();
//            }
//        }
//    }


//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//        googleMap.getUiSettings().setRotateGesturesEnabled(false);
//        //if you need to disable zooming
//        googleMap.getUiSettings().setZoomGesturesEnabled(false);
//
//        //now zooming and rotation now working
//
//        //we can also customize map
//        //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        this.googleMap=googleMap;
//        //let fixed map loading problem
//        // i missed api key
//
//    }

    private void saveImage(Bitmap bitmap) {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            ContentValues values = contentValues();
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + getString(R.string.app_name));
            values.put(MediaStore.Images.Media.IS_PENDING, true);

            Uri uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try {
                    saveImageToStream(bitmap, getContext().getContentResolver().openOutputStream(uri));
                    values.put(MediaStore.Images.Media.IS_PENDING, false);
                    getContext().getContentResolver().update(uri, values, null, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } else {
            File directory = new File(Environment.getExternalStorageDirectory().toString() + '/' + getString(R.string.app_name));

            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".png";
            File file = new File(directory, fileName);
            try {
                saveImageToStream(bitmap, new FileOutputStream(file));
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private ContentValues contentValues() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        }
        return values;
    }

    private void saveImageToStream(Bitmap bitmap, OutputStream outputStream) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}