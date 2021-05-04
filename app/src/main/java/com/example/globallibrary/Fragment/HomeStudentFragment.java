package com.example.globallibrary.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.globallibrary.Activity.StudentAttandance;
import com.example.globallibrary.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
// pending
//1. store phone number locally in phone and keep student user loged in for now user have to log in every time

public class HomeStudentFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {

    RecyclerView recyclerView;
    String StudentId;
    String branchId;
    TextView setQuote;
    MaterialButton showAttandance;
    MaterialButton MarkAttandance;
    MaterialButton MarkDone;
    LinearLayout Marked;
    LinearLayout NotMaked;
    LinearLayout MainDilog;
    ProgressBar progressBar;

    AlertDialog alertDialog;
    TextView Factdisplay;
    MaterialButton MoreFacts;
    RequestQueue queue;


    private static final int PLAY_SERVICE_REQUEST = 9000;
    private static final int PERMISSION_CODE = 101;
    String[] permissions_all={Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
    LocationManager locationManager;
    boolean isGpsProvider;
    boolean isNetworkProvider;
    GoogleApiClient googleApiClient;
    Location location;
    GoogleMap googleMap;


    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_student_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());


        StudentId = getArguments().getString("StudentId");
        branchId = getArguments().getString("BranchId");
        MoreFacts = view.findViewById(R.id.more_facts);
        Factdisplay = view.findViewById(R.id.fact_display);
        DisplayFact();
        MoreFacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DisplayFact();

            }
        });


        MarkAttandance = view.findViewById(R.id.mark_attandance);
        MarkAttandance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(getActivity() , Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },100);
                }


                ViewGroup viewGroup = getView().findViewById(android.R.id.content);


                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dilog_mark_attandance, viewGroup, false);


                //Now we need an AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);

                //finally creating the alert dialog and displaying it
                alertDialog = builder.create();


                alertDialog.show();
                MarkDone = alertDialog.findViewById(R.id.mark_done);
                Marked = alertDialog.findViewById(R.id.marked);
                NotMaked = alertDialog.findViewById(R.id.not_marked);
                MainDilog = alertDialog.findViewById(R.id.main_dilog);
                 progressBar = alertDialog.findViewById(R.id.progress_mark_attandance);
                Marked.setVisibility(View.GONE);
                MainDilog.setVisibility(View.VISIBLE);
                NotMaked.setVisibility(View.GONE);
                MarkDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!checkPlayServiceInstalled()){
                            return;
                        }

                        //now checking permission and request permission

                        if(Build.VERSION.SDK_INT>=23){
                            if(checkPermission()){
                                getDeviceLocation();
                            }
                            else{
                                requestPermission();
                            }
                        }
                        else{
                            getDeviceLocation();
                        }

//                        SupportMapFragment supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
//                        supportMapFragment.getMapAsync(MapWithPlayServiceLocationActivity.this);




                    }
                });


            }
        });


        Log.d("phone", "onViewCreated: " + StudentId); //getting unique phone number from sigh in page.
        setQuote = view.findViewById(R.id.show_quote);
        showAttandance = view.findViewById(R.id.show_attandance);
        showAttandance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , StudentAttandance.class);
                intent.putExtra("StudentId" , StudentId);
                intent.putExtra("BranchId" , branchId);
                startActivity(intent);
            }
        });
        DocumentReference docIdRef1 = firestore.collection("/Branches/" ).document(branchId.trim());
        docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                        setQuote.setText(document.getString("Quote"));

                    } else {

                        Log.d("TAG", "onComplete: not possible" );

                    }
                } else {

                }
            }
        });




    }

    void DisplayFact()
    {


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://numbersapi.com/random/trivia",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        Factdisplay.setText(response.toString());


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onResponse: String : " + error);

            }
        });
        queue.add(stringRequest);

    }

    private void getDeviceLocation() {
        locationManager=(LocationManager)getActivity().getSystemService(Service.LOCATION_SERVICE);
        isGpsProvider=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkProvider=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(!isGpsProvider && !isNetworkProvider){
            //showing setting for enable gps
            showSettingAlert();
        }
        else{
            GetLocationData();
        }
    }

    private void GetLocationData() {
        googleApiClient=new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        googleApiClient.connect();
    }
    private void showSettingAlert() {
        AlertDialog.Builder al=new AlertDialog.Builder(getActivity());
        al.setTitle("Enable GPS");
        al.setMessage("Please Enable GPS");
        al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        });
        al.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        al.show();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(),permissions_all,PERMISSION_CODE);
    }

    private boolean checkPermission() {
        for(int i=0;i<permissions_all.length;i++){
            int result= ContextCompat.checkSelfPermission(getActivity(),permissions_all[i]);
            if(result== PackageManager.PERMISSION_GRANTED){
                continue;
            }
            else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    getDeviceLocation();
                }
                else{
                    Toast.makeText(getActivity(), "Permission Failed", Toast.LENGTH_SHORT).show();
                }
        }
    }
    private boolean checkPlayServiceInstalled() {
        GoogleApiAvailability apiAvailability=GoogleApiAvailability.getInstance();
        int result=apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if(result!= ConnectionResult.SUCCESS){
            if(apiAvailability.isUserResolvableError(result)){
                apiAvailability.getErrorDialog(getActivity(),result,PLAY_SERVICE_REQUEST).show();
                return false;
            }
            else{
                return false;
            }
        }
        else{
            return true;
        }
    }

    @SuppressWarnings(value = "MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        location=LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        DocumentReference docIdRef = firestore.collection("Branches" ).document(branchId.trim());
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                        GeoPoint geoPoint = document.getGeoPoint("Location");
                        Location location1 = new Location("");
                        location1.setLatitude(geoPoint.getLatitude());
                        location1.setLongitude(geoPoint.getLongitude());

                        Toast.makeText(getActivity() , "distance : "  + location.distanceTo(location1) , Toast.LENGTH_LONG).show();

                        //here de can compare the dist with the radius given by branch
                        double radi = document.getDouble("Radius");

                        if(radi  > location.distanceTo(location1) && location.distanceTo(location1)!=0)
                        {
                            Marked.setVisibility(View.VISIBLE);
                            MainDilog.setVisibility(View.GONE);
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            String formattedDate = df.format(c);
                            String studentDoc = formattedDate.substring(3,10);
                            String doc = formattedDate.substring(0,2);
                            Log.d("TAG", "onComplete: check 1" + studentDoc + " " + doc + " " + formattedDate);
                            DocumentReference docIdRef = firestore.collection("/Branches/" + branchId.trim() + "/Attandance/" ).document(formattedDate.trim());
                            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            firestore.collection("Branches").document(branchId).collection("Attandance").document(formattedDate).update(StudentId , true);



                                        } else {
                                            Map<String,Object> allDetails = new HashMap<>();
                                            allDetails.put(StudentId, true);
                                            firestore.collection("Branches").document(branchId).collection("Attandance").document(formattedDate).set(allDetails);

                                            Log.d("TAG", "onComplete: not possible" );

                                        }
                                    } else {

                                    }
                                }
                            });
                            DocumentReference docIdRef1 = firestore.collection("/Branches/" + branchId.trim() + "/StudentDetails/"  + StudentId.trim() + "/Attandance/").document(studentDoc.trim());
                            docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            firestore.collection("Branches").document(branchId).collection("StudentDetails").document(StudentId).collection("Attandance").document(studentDoc.trim()).update(doc , true);




                                        } else {
                                            Map<String,Object> allDetails = new HashMap<>();
                                            allDetails.put(doc, true);
                                            firestore.collection("Branches").document(branchId).collection("StudentDetails").document(StudentId).collection("Attandance").document(studentDoc).set(allDetails);

                                            Log.d("TAG", "onComplete: not possible" );

                                        }
                                    } else {

                                    }
                                }
                            });
                        }
                        else
                        {
                            NotMaked.setVisibility(View.VISIBLE);
                            MainDilog.setVisibility(View.GONE);
                        }



                    } else {

                        Log.d("TAG", "onComplete: not possible" );

                    }
                } else {

                }
            }
        });
        if(location!=null){
//            Toast.makeText(getActivity(), "Lat : "+location.getLatitude()+" Lng "+location.getLongitude(), Toast.LENGTH_SHORT).show();
            if(googleMap!=null){
                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10f));
            }
        }
        startLocationUpdates();

    }
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        LocationRequest locationRequest=new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //10 sec
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this::onLocationChanged);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    private void stopLocationUpdates() {
        if(googleApiClient!=null){
            if(googleApiClient.isConnected()){
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this::onLocationChanged);
                googleApiClient.disconnect();
            }
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        //if you need to disable zooming
        googleMap.getUiSettings().setZoomGesturesEnabled(false);

        //now zooming and rotation now working

        //we can also customize map
        //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap=googleMap;
        //let fixed map loading problem
        // i missed api key

    }

    @Override
    public void onLocationChanged(Location location) {




//        Toast.makeText(getActivity(), "Lat : "+location.getLatitude()+" Lng "+location.getLongitude(), Toast.LENGTH_SHORT).show();
        if(googleMap!=null){
            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10f));
        }

    }
}

