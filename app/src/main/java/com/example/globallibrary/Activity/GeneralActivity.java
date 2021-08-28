package com.example.globallibrary.Activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.globallibrary.Fragment.BranchProfileFragment;
import com.example.globallibrary.Fragment.BranchStudentFragment;
import com.example.globallibrary.Fragment.FeeBranchFragment;
import com.example.globallibrary.Fragment.FeeStudentFragment;
import com.example.globallibrary.Fragment.HomeBranchFragment;
import com.example.globallibrary.Fragment.HomeStudentFragment;
import com.example.globallibrary.Fragment.NoticeBranchFragment;
import com.example.globallibrary.Fragment.NoticeStudentFragment;
import com.example.globallibrary.Fragment.ProfileStudentFragment;
import com.example.globallibrary.Fragment.QuizStudentFragment;
import com.example.globallibrary.Fragment.StudentAttandanceFragment;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GeneralActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    DrawerLayout drawerLayout;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    StorageReference storageReference;
    ImageView branchImage;
    TextView BranchNameAtNarBar;
//    TextView AddressAtNavBar;
    ImageButton Slider;
    TextView ToolbarText;
//CircularImageView BranchImage;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;
    ChipNavigationBar bottomNavBar;
    String access;
    String branchId;
    String studentId;
    MaterialButton PastQuiz;

    LinearLayout mainFrame;

    MaterialButton NewNotification123;


    ImageButton HomeIcon;
    ImageButton ProfileIcon;
    ImageButton FeeIcon , BranchProfile , attendence , registred ;

    ImageButton NoticeIcon;
    MaterialButton AddNotice;



    private static final String KEY_ACCESS = "access";
    private static final String SHARED_PREF = "PREF";
    private static final String KEY_BRANCH_ID = "id";
    private static final String KEY_STUDENT_ID = "id_student";
    private static final String KEY_STUDENT_B_ID = "id_branch";
    SharedPreferences sharedPreferences;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.main_color));
        }
//        View decorView = getWindow().getDecorView(); //set status background black
//        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);


        setContentView(R.layout.activity_general);
        sharedPreferences  = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        Log.d("TAG", "onCreate: cheking1" + sharedPreferences.getString(KEY_STUDENT_ID , null));
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.slider);
//        BranchImage = findViewById(R.id.toolbar_branch_Image);
        NewNotification123 = findViewById(R.id.add_new_notification);


        mainFrame = findViewById(R.id.main_frame);

        PastQuiz = findViewById(R.id.past_quiz);
        HomeIcon = findViewById(R.id.home_toolbar);
        FeeIcon  = findViewById(R.id.fee_toolbar);
        ProfileIcon = findViewById(R.id.profile_toolbar);
        NoticeIcon = findViewById(R.id.nav_toolbar);
        BranchProfile = findViewById(R.id.profile_branch_toolbar);
        attendence = findViewById(R.id.nav_toolbar_attendence);
        registred = findViewById(R.id.nav_toolbar_registred);
        AddNotice = findViewById(R.id.add_new_notification);






        PastQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(GeneralActivity.this  ,QuizReportStudent.class);
                studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                intent.putExtra("StudentId" , studentId);
                intent.putExtra("BranchId"  , branchId);
                startActivity(intent);

            }
        });
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull @NotNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull @NotNull View drawerView) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.dark_main_color, getApplicationContext().getTheme()));
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.dark_main_color));
                }
                View decorView = getWindow().getDecorView(); //set status background black
                decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.dark_main_color));
                }

            }

            @Override
            public void onDrawerClosed(@NonNull @NotNull View drawerView) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.main_color, getApplicationContext().getTheme()));
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.main_color));
                }
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.main_color));
                }

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        navigationView.bringToFront();
        Slider = findViewById(R.id.Slider_menu);
        ToolbarText = findViewById(R.id.main_text_branch);
        View NavView = navigationView.getHeaderView(0);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BranchNameAtNarBar = NavView.findViewById(R.id.studnet_branch_name);
//        AddressAtNavBar = NavView.findViewById(R.id.student_branch_address);
        branchImage = NavView.findViewById(R.id.BranchImage_in_Branch);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        toggle.setDrawerIndicatorEnabled(false);
        NewNotification123.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: " + "possible");
                Intent intent = new Intent(GeneralActivity.this, NewNotification.class);
                intent.putExtra("branchId", branchId);
                startActivity(intent);

            }
        });

        drawerLayout.addDrawerListener(toggle);

        Slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });


//        access = getIntent().getStringExtra("user");
        access = sharedPreferences.getString(KEY_ACCESS , null);
        Log.d("TAG", "onCreate: accessVal" + access);
        storage = FirebaseStorage.getInstance();
        studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
        branchId = sharedPreferences.getString(KEY_BRANCH_ID , null);
        Log.d("TAG", "onCreate: checking 1 2" + branchId);
        storageReference = storage.getReference();
        if (access.equals("StudentAccess")) {
            studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
            DocumentReference docIdRef1 = firebaseFirestore.collection("/Students/" ).document(studentId.trim());
            docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {


                        } else {


                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            finish();
                            Intent intent = new Intent(GeneralActivity.this, AuthenticationActivity.class);
                            startActivity(intent);



                        }
                    } else {

                    }
                }
            });
            bottomNavBar = findViewById(R.id.bottom_navbar_student);
            bottomNavBar.setVisibility(View.VISIBLE);
            bottomNavBar.setItemSelected(R.id.bottom_nav_home, true);
            ChipNavigationBar chipNavigationBar = findViewById(R.id.bottom_navbar_branch);
            chipNavigationBar.setVisibility(View.GONE);

            studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
            branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
            Calendar calendar = Calendar.getInstance();
            int Month = calendar.get(Calendar.MONTH);
            int Year = calendar.get(Calendar.YEAR);

            DocumentReference docIdRef3= firebaseFirestore.collection("/Branches/" + branchId.trim() + "/StudentDetails" ).document(studentId.trim());
            docIdRef3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {


                            String id = studentId + "-" + String.valueOf(Month+1) + "-" + String.valueOf(Year);
                            DocumentReference docIdRef1 = firebaseFirestore.collection("/Branches/" + branchId.trim() + "/Fee/" ).document(id);
                            docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document1 = task.getResult();
                                        if (document1.exists()) {
                                        } else {
                                            java.util.Date currentTime = Calendar.getInstance().getTime();
                                            Map<String,Object> Fee = new HashMap<>();
                                            Fee.put("StudentId" , studentId);
                                            Fee.put("Date" , document.getDouble("JoinDate"));
                                            Fee.put("Month" ,Month+1 );
                                            Fee.put("Year" , Year);
                                            Fee.put("Status" , false);
                                            Fee.put("Amount" , document.getDouble("Amount"));
                                            Fee.put("Sortthis" , currentTime);

                                            Map<String,Object> Fee1 = new HashMap<>();
                                            Fee1.put("Date" , document.getDouble("JoinDate"));
                                            Fee1.put("Month" ,Month+1 );
                                            Fee1.put("Year" , Year);
                                            Fee1.put("Status" , false);
                                            Fee1.put("Amount"  , document.getDouble("Amount"));
                                            Fee1.put("Sortthis" , currentTime);
                                            firebaseFirestore.collection("Branches").document(branchId).collection("Fee").document(id).set(Fee);
                                            firebaseFirestore.collection("Branches").document(branchId).collection("StudentDetails").document(studentId).collection("Fee").document(id).set(Fee1);

                                            Log.d("TAG", "onComplete: not possible" );

                                        }
                                    } else {

                                    }
                                }
                            });


                        } else {

                            Log.d("TAG", "onComplete: not possible" );

                        }
                    } else {

                    }
                }
            });










                            DocumentReference docIdRef = firebaseFirestore.collection("/Branches/" + branchId.trim() + "/StudentDetails" ).document(studentId.trim());
                            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {


                                            BranchNameAtNarBar.setText(document.getString("FullName"));
                                            String URL = "Student/" + studentId;
                                            storageReference.child(URL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Uri downloadUrl = uri;
                                                    String s = downloadUrl.toString();
                                                    Log.d("hello guys", "onSuccess: " + s);
                                                    Picasso.get().load(s).into(branchImage);

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    branchImage.setImageResource(R.drawable.student_logo);
                                                    // Handle any errors
                                                }
                                            });

//                                            AddressAtNavBar.setText(document.getString("Discreption"));

                                        } else {

                                            Log.d("TAG", "onComplete: not possible" );

                                        }
                                    } else {

                                    }
                                }
                            });

            Bundle bundle = new Bundle();
            Log.d("TAG", "onCreate: checking" + sharedPreferences.getString(KEY_STUDENT_ID , null));
            bundle.putString("StudentId", studentId);
            bundle.putString("BranchId" , branchId);
            mainFrame.setBackgroundColor(Color.WHITE);
            HomeStudentFragment homeStudentFragment = new HomeStudentFragment();
            GoVisibility();
            HomeIcon.setVisibility(View.VISIBLE);
            homeStudentFragment.setArguments(bundle);
            ToolbarText.setText("Home");

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeStudentFragment , "HomeFragment").commit();
        } else {
            GoVisibility();

            HomeIcon.setVisibility(View.VISIBLE);
            bottomNavBar = findViewById(R.id.bottom_navbar_branch);
            bottomNavBar.setVisibility(View.VISIBLE);
            bottomNavBar.setItemSelected(R.id.bottom_nav_home, true);
            ChipNavigationBar chipNavigationBar = findViewById(R.id.bottom_navbar_student);
            chipNavigationBar.setVisibility(View.GONE);
           Menu mnu =  navigationView.getMenu();
           mnu.getItem(2).setVisible(false);


//            branchName = getIntent().getStringExtra("branchName");
            branchId = sharedPreferences.getString(KEY_BRANCH_ID , null);
            DocumentReference docIdRef2 = firebaseFirestore.collection("/BranchAuth/"  ).document(branchId.trim());
            docIdRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                        } else {

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            finish();
                            Intent intent = new Intent(GeneralActivity.this, AuthenticationActivity.class);
                            startActivity(intent);


                            Log.d("TAG", "onComplete: not possible" );

                        }
                    } else {

                    }
                }
            });

            Log.d("TAG", "onCreate: branchName /Branches/" + branchId.trim());
            DocumentReference docIdRef = firebaseFirestore.collection("/Branches/" ).document(branchId.trim());
            docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {


                            BranchNameAtNarBar.setText(document.getString("BranchName"));

                           String  URL = "Branches/"  + document.getString("BranchName");

                            storageReference.child(URL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL for 'users/me/profile.png'
//                Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                                    /// The string(file link) that you need
//
                                    Uri downloadUrl = uri;
                                    String s = downloadUrl.toString();
                                    Log.d("hello guys", "onSuccess: " + s);
                                    Picasso.get().load(s).into(branchImage);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
//                                    branchImage.setImageResource(R.drawable.branch_owner);

                                }
                            });
//                            AddressAtNavBar.setText(document.getString("LibraryAddress"));

                        } else {

                            Log.d("TAG", "onComplete: not possible" );

                        }
                    } else {

                    }
                }
            });




            ToolbarText.setText("Home");
            Bundle bundle = new Bundle();
            bundle.putString("branchId", branchId);
            HomeBranchFragment homeBranchFragment = new HomeBranchFragment();
            homeBranchFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeBranchFragment , "HomeFragment").commit();
        }
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        bottonMenu();
        slidermenu();
    }



    private void slidermenu() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.slider_fees:
                       if(access.equals("StudentAccess"))
                       {
                           fragment = new FeeStudentFragment();
                           ToolbarText.setText("Fee Status");
                           bottomNavBar.setItemSelected(R.id.bottom_nav_home , false);
                           bottomNavBar.setItemSelected(R.id.bottom_nav_Profile , false);
                           bottomNavBar.setItemSelected(R.id.bottom_nav_home_notice , false);
                           bottomNavBar.setItemSelected(R.id.bottom_nav_attandance , false);
                           bottomNavBar.setItemSelected(R.id.bottom_nav_quiz , false);

                           mainFrame.setBackgroundColor(getColor(R.color.main_color));
                           studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                           branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                           Bundle bundle = new Bundle();
                           GoVisibility();
                           FeeIcon.setVisibility(View.VISIBLE);
                           bundle.putString("StudentId" , studentId);
                           bundle.putString("BranchId" , branchId);
                           fragment.setArguments(bundle);
                       }
                       else
                       {
                           fragment = new FeeBranchFragment();
                           GoVisibility();
                           ToolbarText.setText("Fee Status");
                           FeeIcon.setVisibility(View.VISIBLE);
                           branchId  = sharedPreferences.getString(KEY_BRANCH_ID , null);
                           Bundle bundle = new Bundle();
                           bundle.putString("BranchId" , branchId);
                           fragment.setArguments(bundle);
                           bottomNavBar.setItemSelected(R.id.bottom_nav_home_students , false);
                           bottomNavBar.setItemSelected(R.id.bottom_nav_home , false);
                           bottomNavBar.setItemSelected(R.id.bottom_nav_Profile , false);
                           bottomNavBar.setItemSelected(R.id.bottom_nav_home_notice , false);
                       }
                        drawerLayout.closeDrawer(GravityCompat.START);

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                        break;
                    case R.id.slider_setting:
                        if (access.equals("StudentAccess")) {
                            Intent intent = new Intent(GeneralActivity.this, Settings.class);
                            studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                            branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                            intent.putExtra("StudentId", studentId);
                            intent.putExtra("BranchId" , branchId);
                            GoVisibility();
                            HomeIcon.setVisibility(View.VISIBLE);
                            drawerLayout.closeDrawer(GravityCompat.START);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(GeneralActivity.this, Settings.class);
                            branchId  = sharedPreferences.getString(KEY_BRANCH_ID , null);
                            intent.putExtra("BranchId", branchId);
                            Log.d("TAG", "onNavigationItemSelected: setting activity" + branchId);
                            drawerLayout.closeDrawer(GravityCompat.START);
                            GoVisibility();
                            HomeIcon.setVisibility(View.VISIBLE);
                            startActivity(intent);
                        }

                        break;
                    case R.id.slider_logout:
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        finish();
                        Intent intent = new Intent(GeneralActivity.this, AuthenticationActivity.class);
                        startActivity(intent);
                        Toast.makeText(GeneralActivity.this, "Log out Sucessfully", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.quiz_report:
                         intent = new Intent(GeneralActivity.this  ,QuizReportStudent.class);
                        studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                        branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                        intent.putExtra("StudentId" , studentId);
                        intent.putExtra("BranchId"  , branchId);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }
                return true;
            }
        });
    }

    private void bottonMenu() {
        bottomNavBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.bottom_nav_home:
                        if (access.equals("StudentAccess")) {
                            fragment = new HomeStudentFragment();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                            }
                            ToolbarText.setText("Home");
                            mainFrame.setBackgroundColor(Color.WHITE);
                            Bundle bundle = new Bundle();
                            GoVisibility();
                            HomeIcon.setVisibility(View.VISIBLE);

                            findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                            studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                            branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                            bundle.putString("StudentId" , studentId);
                            bundle.putString("BranchId" , branchId);
                            fragment.setArguments(bundle);
                        } else {
                            GoVisibility();
                            HomeIcon.setVisibility(View.VISIBLE);

                            fragment = new HomeBranchFragment();
                            Bundle bundle = new Bundle();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.main_color));
                            }
                            ToolbarText.setText("Home");
                            mainFrame.setBackgroundColor(getColor(R.color.main_color));
                            PastQuiz.setVisibility(View.GONE);
                            findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                            bundle.putString("branchId", branchId);
                            fragment.setArguments(bundle);
                        }
                        break;
                    case R.id.bottom_nav_Profile:
                        if (access.equals("StudentAccess")) {
                           GoVisibility();
                           ProfileIcon.setVisibility(View.VISIBLE);
                            fragment = new ProfileStudentFragment();
                            mainFrame.setBackgroundColor(getColor(R.color.main_color));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.main_color));
                            }
                            ToolbarText.setText("My Profile");
                            Bundle bundle = new Bundle();
                            studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                            branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                            bundle.putString("StudentId", studentId);
                            bundle.putString("BranchId" , branchId);
                            fragment.setArguments(bundle);
                        } else {
                            GoVisibility();
                            BranchProfile.setVisibility(View.VISIBLE);
                            fragment = new BranchProfileFragment();
                            Bundle bundle = new Bundle();
                            ToolbarText.setText("My Profile");
                            mainFrame.setBackgroundColor(getColor(R.color.main_color));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.main_color));
                            }
                            findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                            bundle.putString("branchId", branchId);
                            fragment.setArguments(bundle);
                        }
                        break;
                    case R.id.bottom_nav_home_notice:
                        if (access.equals("StudentAccess")) {
                            GoVisibility();
                            NoticeIcon.setVisibility(View.VISIBLE);
                            fragment = new NoticeStudentFragment();
                            mainFrame.setBackgroundColor(getColor(R.color.main_color));
                            ToolbarText.setText("Notice");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.main_color));
                            }
                            findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                            studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                            branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                            Bundle bundle = new Bundle();
                            bundle.putString("StudentId", studentId);
                            bundle.putString("BranchId" , branchId);
                            fragment.setArguments(bundle);
                        } else {
                             GoVisibility();
                             AddNotice.setVisibility(View.VISIBLE);
                            fragment = new NoticeBranchFragment();
                            Bundle bundle = new Bundle();
                            mainFrame.setBackgroundColor(getColor(R.color.main_color));
                            ToolbarText.setText("Notice");
                            PastQuiz.setVisibility(View.GONE);
                            findViewById(R.id.add_new_notification).setVisibility(View.VISIBLE);
                            bundle.putString("branchId", branchId);
                            fragment.setArguments(bundle);
                        }
                        break;
                    case R.id.bottom_nav_quiz:
                        GoVisibility();
                        PastQuiz.setVisibility(View.VISIBLE);
                        if (access.equals("StudentAccess")) {
                            fragment = new QuizStudentFragment();
                            mainFrame.setBackgroundColor(getColor(R.color.main_color));
                            ToolbarText.setText("Quiz");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.main_color));
                            }
                            Bundle bundle = new Bundle();
                            studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                            branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                            bundle.putString("StudentId", studentId);
                            bundle.putString("BranchId" , branchId);
                            fragment.setArguments(bundle);
                            break;
                        }
                    case R.id.bottom_nav_home_students:
                        ToolbarText.setText("Registered Students");
                        mainFrame.setBackgroundColor(getColor(R.color.main_color));
                        GoVisibility();
                        registred.setVisibility(View.VISIBLE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.main_color));
                        }
                        fragment  = new BranchStudentFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("branchId" , branchId);
                        fragment.setArguments(bundle);
                        break;
                    case R.id.bottom_nav_attandance:
                        GoVisibility();
                        attendence.setVisibility(View.VISIBLE);
                        ToolbarText.setText("Attendence");
                        mainFrame.setBackgroundColor(getColor(R.color.main_color));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.main_color));
                        }
                        fragment  = new StudentAttandanceFragment();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("StudentId", studentId);
                        bundle1.putString("BranchId" , branchId);
                        fragment.setArguments(bundle1);
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
    }

    @Override
    public void onBackPressed() {

        Fragment myFragment = getSupportFragmentManager().findFragmentByTag("HomeFragment");
        if (myFragment != null && myFragment.isVisible()) {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();

            // add your code here
        }
        else
        {
            bottomNavBar.setItemSelected(R.id.bottom_nav_home , true);
            if (access.equals("StudentAccess")) {
                Fragment fragment = new HomeStudentFragment();
                mainFrame.setBackgroundColor(Color.WHITE);
                Bundle bundle = new Bundle();
               GoVisibility();
               HomeIcon.setVisibility(View.VISIBLE);
                ToolbarText.setText("Home");


                findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                bundle.putString("StudentId" , studentId);
                bundle.putString("BranchId" , branchId);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment , "HomeFragment").commit();
            } else {
                GoVisibility();
                HomeIcon.setVisibility(View.VISIBLE);
                Fragment fragment = new HomeBranchFragment();
                Bundle bundle = new Bundle();
                ToolbarText.setText("Home");
                mainFrame.setBackgroundColor(getColor(R.color.main_color));
                PastQuiz.setVisibility(View.GONE);
                findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                bundle.putString("branchId", branchId);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment , "HomeFragment").commit();
            }
        }



    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return true;
    }



    void GoVisibility()
    {
        PastQuiz.setVisibility(View.GONE);
        HomeIcon.setVisibility(View.GONE);
        FeeIcon.setVisibility(View.GONE);
        ProfileIcon.setVisibility(View.GONE);
        NoticeIcon.setVisibility(View.GONE);
        BranchProfile.setVisibility(View.GONE);
        attendence.setVisibility(View.GONE);
        registred.setVisibility(View.GONE);
        AddNotice.setVisibility(View.GONE);
        FeeIcon.setVisibility(View.GONE);
    }

 



}