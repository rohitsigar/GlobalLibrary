package com.example.globallibrary.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.globallibrary.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class SettingFragmentBranch extends Fragment {

    Button Passward;
    Button ContactNumber;
    String BranchId;
    Button MarkLocation;
    Button ChangeLocation;
    Button Done;
    EditText Radi;
    AlertDialog alertDialog;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FusedLocationProviderClient mFusedLocationClient;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FusedLocationProviderClient fusedLocationProviderClient;
    Button ChangeFee;







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_branch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        BranchId = getArguments().getString("BranchId");
        Passward = view.findViewById(R.id.change_passward);
        ContactNumber = view.findViewById(R.id.change_phone_no);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        MarkLocation = view.findViewById(R.id.mark_location);
        ChangeLocation = view.findViewById(R.id.change_location);
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
             Button Change = alertDialog.findViewById(R.id.button_change);
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
                     firebaseFirestore.collection("Branches").document(BranchId).update("DefaultAmount" ,Double.parseDouble(FeeAmount.getText().toString()));
                     Toast.makeText(getActivity() , "Fee is Sucessfully Changed to " +FeeAmount.getText().toString()  , Toast.LENGTH_SHORT).show();
                 }
             });




         }
     });


        DocumentReference docIdRef = firebaseFirestore.collection("/Branches/" ).document(BranchId.trim());
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        if(document.getDouble("Longitude")!=null)
                        {
                            MarkLocation.setVisibility(View.GONE);
                            ChangeLocation.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            MarkLocation.setVisibility(View.VISIBLE);
                            ChangeLocation.setVisibility(View.GONE);
                        }



                    } else {

                        Log.d("TAG", "onComplete: not possible" );

                    }
                } else {

                }
            }
        });
        MarkLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = getView().findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.add_location_branch_dilog, viewGroup, false);


                //Now we need an AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);

                //finally creating the alert dialog and displaying it
                alertDialog = builder.create();
                alertDialog.show();
                Done = alertDialog.findViewById(R.id.buttonOk);
                Radi = alertDialog.findViewById(R.id.radious);
                Done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Radi.getText().toString()==null)
                        {
                            Toast.makeText(getActivity(),"Please Enter Radious",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            if(getActivity().getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED)
                            {
                                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        if(location!=null)
                                        {
                                            double lati =location.getLatitude();
                                            double longi = location.getLongitude();
                                            GeoPoint geoPoint = new GeoPoint(lati , longi);
                                            int i = Integer.parseInt(Radi.getText().toString().trim());
                                            firebaseFirestore.collection("Branches/").document(BranchId).update("Location" , geoPoint);
                                            firebaseFirestore.collection("Branches/").document(BranchId).update("Radius" , i);
                                            Toast.makeText(getActivity() , "Location: " + lati + " " + longi , Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                        }

                                    }
                                });
                            }
                            else
                            {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                            }
                        }

                    }
                });

            }
        });
        ChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = getView().findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.add_location_branch_dilog, viewGroup, false);


                //Now we need an AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);

                //finally creating the alert dialog and displaying it
                alertDialog = builder.create();
                alertDialog.show();
                Done = alertDialog.findViewById(R.id.buttonOk);
                Radi = alertDialog.findViewById(R.id.radious);
                Done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Radi.getText().toString()==null)
                        {
                            Toast.makeText(getActivity(),"Please Enter Radious",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            if(getActivity().getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION )== PackageManager.PERMISSION_GRANTED)
                            {
                                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        if(location!=null)
                                        {
                                            double lati =location.getLatitude();
                                            double longi = location.getLongitude();
                                            int i = Integer.parseInt(Radi.getText().toString().trim());
                                            firebaseFirestore.collection("Branches/").document(BranchId).update("Longitude" , longi);
                                            firebaseFirestore.collection("Branches/").document(BranchId).update("Latitude" , lati);
                                            firebaseFirestore.collection("Branches/").document(BranchId).update("Radius" , i);
                                            Toast.makeText(getActivity() , "Location: is SucessFully Marked" , Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();

                                        }

                                    }
                                });
                            }
                            else
                            {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                            }
                        }

                    }
                });
            }
        });




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


}