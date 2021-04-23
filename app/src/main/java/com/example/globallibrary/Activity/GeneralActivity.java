package com.example.globallibrary.Activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.example.globallibrary.Fragment.FeeStudentFragment;
import com.example.globallibrary.Fragment.FineStudentFragment;
import com.example.globallibrary.Fragment.HomeBranchFragment;
import com.example.globallibrary.Fragment.HomeStudentFragment;
import com.example.globallibrary.Fragment.NoticeBranchFragment;
import com.example.globallibrary.Fragment.NoticeStudentFragment;
import com.example.globallibrary.Fragment.ProfileStudentFragment;
import com.example.globallibrary.Fragment.QuizBranchFragment;
import com.example.globallibrary.Fragment.QuizStudentFragment;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

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
    ImageButton NewNotification123;

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


        setContentView(R.layout.activity_general);

        sharedPreferences  = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        Log.d("TAG", "onCreate: cheking1" + sharedPreferences.getString(KEY_STUDENT_ID , null));
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.slider);
//        BranchImage = findViewById(R.id.toolbar_branch_Image);
        NewNotification123 = findViewById(R.id.add_new_notification);
        navigationView.bringToFront();
        Slider = findViewById(R.id.Slider_menu);
        ToolbarText = findViewById(R.id.main_text_branch);
        View NavView = navigationView.getHeaderView(0);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavBar = findViewById(R.id.bottom_navbar);
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

        bottomNavBar.setItemSelected(R.id.bottom_nav_home, true);
//        access = getIntent().getStringExtra("user");
        access = sharedPreferences.getString(KEY_ACCESS , null);
        Log.d("TAG", "onCreate: accessVal" + access);
        storage = FirebaseStorage.getInstance();
        studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
        branchId = sharedPreferences.getString(KEY_BRANCH_ID , null);
        Log.d("TAG", "onCreate: checking 1 2" + branchId);
        storageReference = storage.getReference();
        if (access.equals("StudentAccess")) {
//            phoneNo = getIntent().getStringExtra("contactNumber");
            studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
            branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);

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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeStudentFragment).addToBackStack(null).commit();
        } else {
//            branchName = getIntent().getStringExtra("branchName");
            branchId = sharedPreferences.getString(KEY_BRANCH_ID , null);
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
            bundle.putString("branchName", branchId);
            HomeBranchFragment homeBranchFragment = new HomeBranchFragment();
            homeBranchFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeBranchFragment).addToBackStack(null).commit();
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
                        fragment = new FeeStudentFragment();

                        drawerLayout.closeDrawer(GravityCompat.START);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                        break;
                    case R.id.slider_fine:
                        fragment = new FineStudentFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                        break;
                    case R.id.slider_setting:
                        if (access == "StudentAccess") {
                            Intent intent = new Intent(GeneralActivity.this, Settings.class);
                            studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                            branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                            intent.putExtra("StudentId", studentId);
                            intent.putExtra("BranchId" , branchId);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(GeneralActivity.this, Settings.class);
                            intent.putExtra("branchId", branchId);
                            Log.d("TAG", "onNavigationItemSelected: setting activity" + branchId);
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
                            ToolbarText.setText("Home");
                            findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                            studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                            branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                            bundle.putString("StudentId" , studentId);
                            bundle.putString("BranchId" , branchId);
                            fragment.setArguments(bundle);
                        } else {
                            fragment = new HomeBranchFragment();
                            Bundle bundle = new Bundle();
                            ToolbarText.setText("Home");
                            findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                            bundle.putString("branchId", branchId);
                            fragment.setArguments(bundle);
                        }
                        break;
                    case R.id.bottom_nav_Profile:
                        if (access.equals("StudentAccess")) {
                            fragment = new ProfileStudentFragment();
                            Bundle bundle = new Bundle();
                            studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                            branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                            bundle.putString("StudentId", studentId);
                            bundle.putString("BranchId" , branchId);
                            fragment.setArguments(bundle);
                        } else {
                            fragment = new BranchProfileFragment();
                            Bundle bundle = new Bundle();
                            ToolbarText.setText("Profile");
                            findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                            bundle.putString("branchId", branchId);
                            fragment.setArguments(bundle);
                        }
                        break;
                    case R.id.bottom_nav_home_notice:
                        if (access.equals("StudentAccess")) {
                            fragment = new NoticeStudentFragment();
                            ToolbarText.setText("Notice");
                            findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                            studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                            branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                            Bundle bundle = new Bundle();
                            bundle.putString("StudentId", studentId);
                            bundle.putString("BranchId" , branchId);
                            fragment.setArguments(bundle);
                        } else {
                            fragment = new NoticeBranchFragment();
                            Bundle bundle = new Bundle();
                            ToolbarText.setText("Notice");
                            findViewById(R.id.add_new_notification).setVisibility(View.VISIBLE);
                            bundle.putString("branchId", branchId);
                            fragment.setArguments(bundle);
                        }
                        break;
                    case R.id.bottom_nav_quiz:
                        if (access.equals("StudentAccess")) {
                            fragment = new QuizStudentFragment();
                            Bundle bundle = new Bundle();
                            studentId = sharedPreferences.getString(KEY_STUDENT_ID , null);
                            branchId  = sharedPreferences.getString(KEY_STUDENT_B_ID , null);
                            bundle.putString("StudentId", studentId);
                            bundle.putString("BranchId" , branchId);
                            fragment.setArguments(bundle);
                        } else {
                            fragment = new QuizBranchFragment();
                            Bundle bundle = new Bundle();
                            ToolbarText.setText("Quiz");
                            findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                            bundle.putString("branchId", branchId);
                            fragment.setArguments(bundle);
                        }

                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return true;
    }
}