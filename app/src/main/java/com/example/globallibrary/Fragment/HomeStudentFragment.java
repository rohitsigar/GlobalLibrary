package com.example.globallibrary.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class HomeStudentFragment extends Fragment  {


    String StudentId;
    String branchId;
    TextView setQuote;

    boolean check = false;

    TextView Factdisplay;
    MaterialButton MoreFacts;
    RequestQueue queue;



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

        if (checkPermission()) {
            // if permission is already granted display a toast message
            Toast.makeText(getActivity(), "Permission Granted..", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }




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



        Log.d("phone", "onViewCreated: " + StudentId); //getting unique phone number from sigh in page.
        setQuote = view.findViewById(R.id.show_quote);

        DocumentReference docIdRef1 = firestore.collection("/Branches/").document(branchId.trim());
        docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                        setQuote.setText(document.getString("Quote"));

                    } else {

                        Log.d("TAG", "onComplete: not possible");

                    }
                } else {

                }
            }
        });


    }

    void DisplayFact() {


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

    private boolean checkPermission() {
        // here we are checking two permission that is vibrate
        // and camera which is granted by user and not.
        // if permission is granted then we are returning

        // true otherwise false.
        int Camera = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        int camera_permission = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Context.CAMERA_SERVICE);
        int vibrate_permission = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Context.VIBRATOR_SERVICE);
        return camera_permission == PackageManager.PERMISSION_GRANTED && vibrate_permission == PackageManager.PERMISSION_GRANTED && Camera==PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        // this method is to request
        // the runtime permission.
        int PERMISSION_REQUEST_CODE = 200;
        ActivityCompat.requestPermissions(getActivity(), new String[]{Context.CAMERA_SERVICE,  Context.VIBRATOR_SERVICE , Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // this method is called when user
        // allows the permission to use camera.
        if (grantResults.length > 0) {
            boolean cameraaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean vibrateaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean Camera = grantResults[2] == PackageManager.PERMISSION_GRANTED;
            if (cameraaccepted && vibrateaccepted && Camera) {
                Toast.makeText(getActivity(), "Permission granted..", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Permission Denined \n You cannot use app without providing permssion", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public boolean checkLateORnot(int hour , int minute)
    {

        final boolean[] check1 = new boolean[1];

        DocumentReference docIdRef1 = firestore.collection("/Branches/" ).document(branchId.trim());
        docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        int OpenHour = document.getDouble("OpenHour").intValue();
                        int CloseHour = document.getDouble("CloseHour").intValue();
                        int OpenMinute = document.getDouble("OpenMinute").intValue();
                        int CloseMinute = document.getDouble("CloseMinute").intValue();

                        if(((hour > OpenHour) || (hour==OpenHour && minute >=OpenMinute)) && (hour < CloseHour || (hour== CloseHour && minute <=CloseMinute)))
                        {
                            Log.d("TAG", "onComplete: Hello" + "hahahahahahahaha");

                           check = true;

                           check1[0] = true;
                        }
                        else
                        {
                            check = false;
                        }





                    } else {


                    }
                } else {

                }
            }
        });



        return  check;
    }


}

