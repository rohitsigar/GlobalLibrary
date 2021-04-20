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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    String branchName;
    String phoneNo;
    ImageButton NewNotification123;

    private static final String KEY_ACCESS = "access";
    private static final String SHARED_PREF = "PREF";
    private static final String KEY_BRANCH_NAME = "name";
    private static final String KEY_PHONE_NO = "phoneNo";
    SharedPreferences sharedPreferences;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
// /       getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.main_page_toolbar);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_general);

        sharedPreferences  = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
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
                intent.putExtra("branchName", branchName);
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
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if (access.equals("StudentAccess")) {
//            phoneNo = getIntent().getStringExtra("contactNumber");
            phoneNo = sharedPreferences.getString(KEY_PHONE_NO , null);
            Bundle bundle = new Bundle();
            bundle.putString("phoneNo", phoneNo);
            HomeStudentFragment homeStudentFragment = new HomeStudentFragment();
            homeStudentFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeStudentFragment).addToBackStack(null).commit();
        } else {
//            branchName = getIntent().getStringExtra("branchName");
            branchName = sharedPreferences.getString(KEY_BRANCH_NAME , null);
            Log.d("TAG", "onCreate: branchName" + branchName);
            firebaseFirestore.collection("/Branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                private static final String TAG = "Rohit";

                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult())
                            if (document.getString("BranchName").equals(branchName)) {
                                BranchNameAtNarBar.setText(branchName);
                                AddressAtNavBar.setText(document.getString("LibraryAddress"));
                            }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });

            String URL = "Branches/" + branchName;
//            storageReference.child(URL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    // Got the download URL for 'users/me/profile.png'
////                Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
//                    /// The string(file link) that you need
//                    String s = uri.toString();
//                    Picasso.get().load(s).into(branchImage);
//                    Picasso.get().load(s).into(BranchImage);
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//
//                    // Handle any errors
//                }
//            });
            ToolbarText.setText("Home");
            Bundle bundle = new Bundle();
            bundle.putString("branchName", branchName);
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
                            intent.putExtra("phoneNo", phoneNo);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(GeneralActivity.this, Settings.class);
                            intent.putExtra("branchName", branchName);
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
                            bundle.putString("PhoneNo", phoneNo);
                            fragment.setArguments(bundle);
                        } else {
                            fragment = new HomeBranchFragment();
                            Bundle bundle = new Bundle();
                            ToolbarText.setText("Home");
                            findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                            bundle.putString("branchName", branchName);
                            fragment.setArguments(bundle);
                        }
                        break;
                    case R.id.bottom_nav_Profile:
                        if (access.equals("StudentAccess")) {
                            fragment = new ProfileStudentFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("PhoneNo", phoneNo);
                            fragment.setArguments(bundle);
                        } else {
                            fragment = new BranchProfileFragment();
                            Bundle bundle = new Bundle();
                            ToolbarText.setText("Profile");
                            findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                            bundle.putString("branchName", branchName);
                            fragment.setArguments(bundle);
                        }
                        break;
                    case R.id.bottom_nav_home_notice:
                        if (access.equals("studentAccess")) {
                            fragment = new NoticeStudentFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("PhoneNo", phoneNo);
                            fragment.setArguments(bundle);
                        } else {
                            fragment = new NoticeBranchFragment();
                            Bundle bundle = new Bundle();
                            ToolbarText.setText("Notice");
                            findViewById(R.id.add_new_notification).setVisibility(View.VISIBLE);
                            bundle.putString("branchName", branchName);
                            fragment.setArguments(bundle);
                        }
                        break;
                    case R.id.bottom_nav_quiz:
                        if (access.equals("studentAccess")) {
                            fragment = new QuizStudentFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("PhoneNo", phoneNo);
                            fragment.setArguments(bundle);
                        } else {
                            fragment = new QuizBranchFragment();
                            Bundle bundle = new Bundle();
                            ToolbarText.setText("Quiz");
                            findViewById(R.id.add_new_notification).setVisibility(View.GONE);
                            bundle.putString("branchName", branchName);
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