package com.example.globallibrary.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
// pending
//1. store phone number locally in phone and keep student user loged in for now user have to log in every time

public class HomeStudentFragment extends Fragment {

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
    LocationManager locationManager;


    TextView DisplayLocation;
    AlertDialog alertDialog;
    TextView Factdisplay;
    MaterialButton MoreFacts;
    RequestQueue queue;
    private Boolean mLocationPermissionsGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private FusedLocationProviderClient mFusedLocationProviderClient;

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

                        final LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                        if (isNetworkEnabled) {
                            Criteria criteria = new Criteria();
                            criteria.setAccuracy(Criteria.ACCURACY_COARSE);

                            if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                                    PackageManager.PERMISSION_GRANTED) &&
                                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                                            PackageManager.PERMISSION_GRANTED) {

                            } else {  Log.d("TAG", "getLocationPermission: getting location permissions");
                                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION};

                                if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                                    if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                                        mLocationPermissionsGranted = true;
                                    }else{
                                        ActivityCompat.requestPermissions(getActivity(),
                                                permissions,
                                                LOCATION_PERMISSION_REQUEST_CODE);
                                    }
                                }else{
                                    ActivityCompat.requestPermissions(getActivity(),
                                            permissions,
                                            LOCATION_PERMISSION_REQUEST_CODE);
                                }


                                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                                try{
                                    if(mLocationPermissionsGranted){

                                        final Task location = mFusedLocationProviderClient.getLastLocation();
                                        location.addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if(task.isSuccessful()){
                                                    Log.d("TAG", "onComplete: found location!");
                                                    Location currentLocation = (Location) task.getResult();

                                                    DocumentReference docIdRef = firestore.collection("Branches").document(branchId.trim());
                                                    docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentSnapshot document = task.getResult();
                                                                if (document.exists()) {

                                                                    GeoPoint geoPoint = document.getGeoPoint("Location");
                                                                    Location location1 = new Location("");
                                                                    location1.setLongitude(geoPoint.getLongitude());
                                                                    location1.setLatitude(geoPoint.getLatitude());

                                                                    Toast.makeText(getActivity() ,String.valueOf(currentLocation.distanceTo(location1)) , Toast.LENGTH_LONG).show();




                                                                } else {

                                                                    Log.d("TAG", "onComplete: not possible" );

                                                                }
                                                            } else {

                                                            }
                                                        }
                                                    });



                                                }else{
                                                    Log.d("TAG", "onComplete: current location is null");
                                                    Toast.makeText(getActivity(), "unable to get current location", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }catch (SecurityException e){
                                    Log.e("TAG", "getDeviceLocation: SecurityException: " + e.getMessage() );
                                }

                            }
                        }


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




}