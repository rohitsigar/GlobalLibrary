package com.example.globallibrary.Activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.globallibrary.Fragment.BranchProfileFragment;
import com.example.globallibrary.Fragment.FeeBranchFragment;
import com.example.globallibrary.Fragment.FeeStudentFragment;
import com.example.globallibrary.Fragment.HomeBranchFragment;
import com.example.globallibrary.Fragment.HomeStudentFragment;
import com.example.globallibrary.Fragment.NoticeBranchFragment;
import com.example.globallibrary.Fragment.NoticeStudentFragment;
import com.example.globallibrary.Fragment.ProfileStudentFragment;
import com.example.globallibrary.Fragment.QuizStudentFragment;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

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
    TextView AddressAtNavBar;
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

    MaterialButton NewNotification123;


    ImageButton HomeIcon;
    ImageButton ProfileIcon;
    ImageButton FeeIcon;



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


        setContentView(R.layout.activity_general);
        sharedPreferences  = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        Log.d("TAG", "onCreate: cheking1" + sharedPreferences.getString(KEY_STUDENT_ID , null));
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.slider);
//        BranchImage = findViewById(R.id.toolbar_branch_Image);
        NewNotification123 = findViewById(R.id.add_new_notification);
        PastQuiz = findViewById(R.id.past_quiz);
        HomeIcon = findViewById(R.id.home_toolbar);
        FeeIcon  = findViewById(R.id.fee_toolbar);
        ProfileIcon = findViewById(R.id.profile_toolbar);
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
        navigationView.bringToFront();
        Slider = findViewById(R.id.Slider_menu);
        ToolbarText = findViewById(R.id.main_text_branch);
        View NavView = navigationView.getHeaderView(0);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BranchNameAtNarBar = NavView.findViewById(R.id.studnet_branch_name);
        AddressAtNavBar = NavView.findViewById(R.id.student_branch_address);
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
                                            Map<String,Object> Fee = new HashMap<>();
                                            Fee.put("StudentId" , studentId);
                                            Fee.put("Date" , document.getDouble("JoinDate"));
                                            Fee.put("Month" ,Month+1 );
                                            Fee.put("Year" , Year);
                                            Fee.put("Status" , false);
                                            Fee.put("Amount" , document.getDouble("Amount"));

                                            Map<String,Object> Fee1 = new HashMap<>();
                                            Fee1.put("Date" , document.getDouble("JoinDate"));
                                            Fee1.put("Month" ,Month+1 );
                                            Fee1.put("Year" , Year);
                                            Fee1.put("Status" , false);
                                            Fee1.put("Amount"  , document.getDouble("Amount"));
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
                                            AddressAtNavBar.setText(document.getString("Discreption"));

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
            HomeStudentFragment homeStudentFragment = new HomeStudentFragment();
            homeStudentFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeStudentFragment , "HomeFragment").commit();
        } else {

            bottomNavBar = findViewById(R.id.bottom_navbar_branch);
            bottomNavBar.setVisibility(View.VISIBLE);
            bottomNavBar.setItemSelected(R.id.bottom_nav_home, true);
            ChipNavigationBar chipNavigationBar = findViewById(R.id.bottom_navbar_student);
            chipNavigationBar.setVisibility(View.GONE);




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
                            AddressAtNavBar.setText(document.getString("LibraryAddress"));

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
                           studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                           branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                           Bundle bundle = new Bundle();
                           OpenFee();
                           bundle.putString("StudentId" , studentId);
                           bundle.putString("BranchId" , branchId);
                           fragment.setArguments(bundle);
                       }
                       else
                       {
                           fragment = new FeeBranchFragment();
                           OpenFee();
                           branchId  = sharedPreferences.getString(KEY_BRANCH_ID , null);
                           Bundle bundle = new Bundle();
                           bundle.putString("BranchId" , branchId);
                           fragment.setArguments(bundle);
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
                            OpenHome();
                            drawerLayout.closeDrawer(GravityCompat.START);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(GeneralActivity.this, Settings.class);
                            branchId  = sharedPreferences.getString(KEY_BRANCH_ID , null);
                            intent.putExtra("BranchId", branchId);
                            Log.d("TAG", "onNavigationItemSelected: setting activity" + branchId);
                            drawerLayout.closeDrawer(GravityCompat.START);
                            OpenHome();
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

                }
                return true;
            }
        });
    }

    private void bottonMenu() {
        bottomNavBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.bottom_nav_home:
                        if (access.equals("StudentAccess")) {
                            fragment = new HomeStudentFragment();
                            Bundle bundle = new Bundle();
                            OpenHome();

                            findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                            studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                            branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                            bundle.putString("StudentId" , studentId);
                            bundle.putString("BranchId" , branchId);
                            fragment.setArguments(bundle);
                        } else {
                            OpenHome();
                            fragment = new HomeBranchFragment();
                            Bundle bundle = new Bundle();
                            ToolbarText.setText("Home");
                            PastQuiz.setVisibility(View.GONE);
                            findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                            bundle.putString("branchId", branchId);
                            fragment.setArguments(bundle);
                        }
                        break;
                    case R.id.bottom_nav_Profile:
                        if (access.equals("StudentAccess")) {
                            OpenProfile();
                            fragment = new ProfileStudentFragment();
                            Bundle bundle = new Bundle();
                            PastQuiz.setVisibility(View.GONE);
                            studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                            branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                            bundle.putString("StudentId", studentId);
                            bundle.putString("BranchId" , branchId);
                            fragment.setArguments(bundle);
                        } else {
                            OpenProfile();
                            fragment = new BranchProfileFragment();
                            Bundle bundle = new Bundle();
                            ToolbarText.setText("Profile");
                            PastQuiz.setVisibility(View.GONE);
                            findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                            bundle.putString("branchId", branchId);
                            fragment.setArguments(bundle);
                        }
                        break;
                    case R.id.bottom_nav_home_notice:
                        if (access.equals("StudentAccess")) {
                            OpenNotification();
                            fragment = new NoticeStudentFragment();
                            ToolbarText.setText("Notice");
                            PastQuiz.setVisibility(View.GONE);
                            findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                            studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                            branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                            Bundle bundle = new Bundle();
                            bundle.putString("StudentId", studentId);
                            bundle.putString("BranchId" , branchId);
                            fragment.setArguments(bundle);
                        } else {
                            OpenNotification();
                            fragment = new NoticeBranchFragment();
                            Bundle bundle = new Bundle();
                            ToolbarText.setText("Notice");
                            PastQuiz.setVisibility(View.GONE);
                            findViewById(R.id.add_new_notification).setVisibility(View.VISIBLE);
                            bundle.putString("branchId", branchId);
                            fragment.setArguments(bundle);
                        }
                        break;
                    case R.id.bottom_nav_quiz:
                        OpenQuiz();
                        if (access.equals("StudentAccess")) {
                            fragment = new QuizStudentFragment();
                            Bundle bundle = new Bundle();
                            PastQuiz.setVisibility(View.VISIBLE);
                            studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                            branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                            bundle.putString("StudentId", studentId);
                            bundle.putString("BranchId" , branchId);
                            fragment.setArguments(bundle);
                        } else {


                        }

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
            if (access.equals("StudentAccess")) {
                Fragment fragment = new HomeStudentFragment();
                Bundle bundle = new Bundle();
                OpenHome();

                findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                bundle.putString("StudentId" , studentId);
                bundle.putString("BranchId" , branchId);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment , "HomeFragment").commit();
            } else {
                OpenHome();
                Fragment fragment = new HomeBranchFragment();
                Bundle bundle = new Bundle();
                ToolbarText.setText("Home");
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
    // sare functions

    void OpenProfile()
    {
        ToolbarText.setText("Profile");
        ProfileIcon.setVisibility(View.VISIBLE);
        HomeIcon.setVisibility(View.GONE);
        FeeIcon.setVisibility(View.GONE);
        NewNotification123.setVisibility(View.GONE);
        PastQuiz.setVisibility(View.GONE);
        MenuItem i  = navigationView.getCheckedItem();
        if(i!=null)
        {
            i.setChecked(false);
        }
        }
    void OpenFee()
    {
        ToolbarText.setText("Fee Status");
        ProfileIcon.setVisibility(View.GONE);
        HomeIcon.setVisibility(View.GONE);
        FeeIcon.setVisibility(View.VISIBLE);
        NewNotification123.setVisibility(View.GONE);
        PastQuiz.setVisibility(View.GONE);
        int i = bottomNavBar.getSelectedItemId();
        bottomNavBar.setItemSelected(i , false);
    }
    void OpenHome()
    {
        ToolbarText.setText("Home");
        ProfileIcon.setVisibility(View.GONE);
        HomeIcon.setVisibility(View.VISIBLE);
        FeeIcon.setVisibility(View.GONE);
        NewNotification123.setVisibility(View.GONE);
        PastQuiz.setVisibility(View.GONE);
        MenuItem i  = navigationView.getCheckedItem();

        bottomNavBar.setItemSelected(R.id.bottom_nav_home , true);
        if(i!=null)
        {
            i.setChecked(false);
        }

    }
    void OpenNotification()
    {
        ToolbarText.setText("Notice");
        ProfileIcon.setVisibility(View.GONE);
        HomeIcon.setVisibility(View.GONE);
        FeeIcon.setVisibility(View.GONE);
        NewNotification123.setVisibility(View.VISIBLE);
        PastQuiz.setVisibility(View.GONE);
        MenuItem i  = navigationView.getCheckedItem();
        if(i!=null)
        {
            i.setChecked(false);
        }
    }
    void OpenQuiz()
    {
        ToolbarText.setText("Quiz");
        ProfileIcon.setVisibility(View.GONE);
        HomeIcon.setVisibility(View.GONE);
        FeeIcon.setVisibility(View.GONE);
        NewNotification123.setVisibility(View.GONE);
        PastQuiz.setVisibility(View.VISIBLE);
        MenuItem i  = navigationView.getCheckedItem();
        if(i!=null)
        {
            i.setChecked(false);
        }
    }

}