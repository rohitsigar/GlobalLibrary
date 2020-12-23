package com.example.globallibrary.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.globallibrary.Fragment.FeeStudentFragment;
import com.example.globallibrary.Fragment.FineStudentFragment;
import com.example.globallibrary.Fragment.HomeStudentFragment;
import com.example.globallibrary.Fragment.NoticeStudentFragment;
import com.example.globallibrary.Fragment.ProfileStudentFragment;
import com.example.globallibrary.Fragment.QuizStudentFragment;
import com.example.globallibrary.R;
import com.google.android.material.navigation.NavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class GeneralActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
  DrawerLayout drawerLayout;
   NavigationView navigationView;
   androidx.appcompat.widget.Toolbar toolbar;
   ChipNavigationBar bottomNavBar;
   Menu menu;
    TextView textView;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        drawerLayout = findViewById(R.id.drawer_layout);
     navigationView = findViewById(R.id.slider);
      navigationView.bringToFront();
      toolbar=findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
      bottomNavBar = findViewById(R.id.bottom_navbar);
     ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open , R.string.close);
   drawerLayout.addDrawerListener(toggle);
  bottomNavBar.setItemSelected(R.id.bottom_nav_home,true);
      getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeStudentFragment()).commit();
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
                switch (item.getItemId())
                {
                    case R.id.slider_fees:
                        fragment = new FeeStudentFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.slider_fine:
                        fragment = new FineStudentFragment();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.slider_setting:
                        Intent intent = new Intent(GeneralActivity.this ,Settings.class);
                        startActivity(intent);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                return true;
            }
        });
    }

    private void bottonMenu() {
        bottomNavBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i)
                {
                    case R.id.bottom_nav_home:
                        fragment = new HomeStudentFragment();
                        break;
                    case R.id.bottom_nav_Profile:
                        fragment = new ProfileStudentFragment();
                        break;
                    case  R.id.bottom_nav_home_notice:
                        fragment = new NoticeStudentFragment();
                        break;
                    case  R.id.bottom_nav_quiz:
                        fragment = new QuizStudentFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });
    }

    @Override
   public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
       {
          drawerLayout.closeDrawer(GravityCompat.START);
      }
       else
       {
          super.onBackPressed();
       }
   }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return true;
    }
}