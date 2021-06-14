package com.example.globallibrary.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;
// pending
//1. store phone number locally in phone and keep student user loged in for now user have to log in every time

public class HomeStudentFragment extends Fragment  {

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
    boolean check = false;

    AlertDialog alertDialog;
    TextView Factdisplay;
    MaterialButton MoreFacts;
    RequestQueue queue;

    TextView First;
    TextView Second;


    private static final int PLAY_SERVICE_REQUEST = 9000;
    private static final int PERMISSION_CODE = 101;
    String[] permissions_all = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    LocationManager locationManager;
    boolean isGpsProvider;
    boolean isNetworkProvider;
    GoogleApiClient googleApiClient;
    Location location;
    GoogleMap googleMap;
    private ScannerLiveView camera;


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
camera = view.findViewById(R.id.camview);


        MarkAttandance = view.findViewById(R.id.mark_attandance);
        MarkAttandance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);
                String s1 = c.toString();
//                int hour = Integer.getInteger(s1.substring(11,13));
//                int minute = Integer.getInteger(s1.substring(14,16));
                Log.d("TAG", "onClick: checking" + s1.substring(11,13) + " "  + s1.substring(14,16) );

                String Hour = s1.substring(11,13);
                String Minute = s1.substring(14,16);
                Log.d("TAG", "onClick: checking " + Hour  + " " + Minute);
                int hour = Integer.parseInt(Hour);
                int minute = Integer.parseInt(Minute);
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
                                    DocumentReference docIdRef = firestore.collection("/Branches/" + branchId.trim() + "/Attandance/").document(formattedDate.trim());

                                    docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    if(document.contains(StudentId.trim()))
                                                    {
                                                        ViewGroup viewGroup = getView().findViewById(android.R.id.content);
                                                        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dilog_mark_attandance, viewGroup, false);
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                        builder.setView(dialogView);
                                                        alertDialog = builder.create();
                                                        alertDialog.show();
                                                        LinearLayout AlreadyMarked = alertDialog.findViewById(R.id.already_marked);
                                                        AlreadyMarked.setVisibility(View.VISIBLE);
                                                    }
                                                    else
                                                    {
                                                        camera.setVisibility(View.VISIBLE);
                                                        ZXDecoder decoder = new ZXDecoder();
                                                        // 0.5 is the area where we have
                                                        // to place red marker for scanning.
                                                        decoder.setScanAreaPercent(0.8);
                                                        // below method will set secoder to camera.
                                                        camera.setDecoder(decoder);
                                                        camera.startScanner();

                                                    }


                                                } else {
                                                    camera.setVisibility(View.VISIBLE);
                                                    ZXDecoder decoder = new ZXDecoder();
                                                    // 0.5 is the area where we have
                                                    // to place red marker for scanning.
                                                    decoder.setScanAreaPercent(0.8);
                                                    // below method will set secoder to camera.
                                                    camera.setDecoder(decoder);
                                                    camera.startScanner();

                                                }
                                            } else {

                                            }
                                        }
                                    });



                                }
                                else
                                {
                                    ViewGroup viewGroup = getView().findViewById(android.R.id.content);
                                    View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dilog_mark_attandance, viewGroup, false);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setView(dialogView);
                                    alertDialog = builder.create();
                                    alertDialog.show();
                                    LinearLayout AlreadyMarked = alertDialog.findViewById(R.id.not_on_time);
                                    AlreadyMarked.setVisibility(View.VISIBLE);

                                }





                            } else {


                            }
                        } else {

                        }
                    }
                });

                    }
                });

        camera.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
                // method is called when scanner is started
                Toast.makeText(getActivity(), "Scanner Started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
                // method is called when scanner is stoped.
                Toast.makeText(getActivity(), "Scanner Stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerError(Throwable err) {
                // method is called when scanner gives some error.
                Toast.makeText(getActivity(), "Scanner Error: " + err.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeScanned(String data) {

                camera.stopScanner();
                // method is called when camera scans the
                // qr code and the data from qr code is
                // stored in data in string format.
                camera.setVisibility(View.GONE);
                ViewGroup viewGroup = getView().findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dilog_mark_attandance, viewGroup, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(dialogView);
                alertDialog = builder.create();
                alertDialog.show();
                Marked = alertDialog.findViewById(R.id.marked);
                NotMaked = alertDialog.findViewById(R.id.not_marked);


                DocumentReference docIdRef1 = firestore.collection("/Branches/" ).document(branchId.trim());
                docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                if(data.equals(document.getString("UniqueQrCode")))
                                {
                                    Marked.setVisibility(View.VISIBLE);
                                    NotMaked.setVisibility(View.GONE);
                                    Date c = Calendar.getInstance().getTime();
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    String formattedDate = df.format(c);
                                    String studentDoc = formattedDate.substring(3, 10);
                                    String doc = formattedDate.substring(0, 2);
                                    DocumentReference docIdRef = firestore.collection("/Branches/" + branchId.trim() + "/Attandance/").document(formattedDate.trim());
                                    docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    firestore.collection("Branches").document(branchId).collection("Attandance").document(formattedDate).update(StudentId, true);


                                                } else {
                                                    Map<String, Object> allDetails = new HashMap<>();
                                                    allDetails.put(StudentId, true);
                                                    firestore.collection("Branches").document(branchId).collection("Attandance").document(formattedDate).set(allDetails);

                                                    Log.d("TAG", "onComplete: not possible");

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
                                    Marked.setVisibility(View.GONE);
                                    NotMaked.setVisibility(View.VISIBLE);
                                }


                            } else {






                            }
                        } else {

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
                Intent intent = new Intent(getActivity(), StudentAttandance.class);
                intent.putExtra("StudentId", StudentId);
                intent.putExtra("BranchId", branchId);
                startActivity(intent);
            }
        });
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
        int camera_permission = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Context.CAMERA_SERVICE);
        int vibrate_permission = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Context.VIBRATOR_SERVICE);
        return camera_permission == PackageManager.PERMISSION_GRANTED && vibrate_permission == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        // this method is to request
        // the runtime permission.
        int PERMISSION_REQUEST_CODE = 200;
        ActivityCompat.requestPermissions(getActivity(), new String[]{Context.CAMERA_SERVICE,  Context.VIBRATOR_SERVICE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // this method is called when user
        // allows the permission to use camera.
        if (grantResults.length > 0) {
            boolean cameraaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean vibrateaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if (cameraaccepted && vibrateaccepted) {
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

