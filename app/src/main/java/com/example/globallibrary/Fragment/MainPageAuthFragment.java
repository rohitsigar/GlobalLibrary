package com.example.globallibrary.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.globallibrary.Adpaters.PagerAdaptorAuth;
import com.example.globallibrary.R;
import com.google.android.material.tabs.TabLayout;

public class MainPageAuthFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdaptorAuth viewPagerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_page_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewPagerAuth);
        viewPagerAdapter = new PagerAdaptorAuth(getChildFragmentManager());

        /////*     add adapter to ViewPager  */////
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs_auth);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabRippleColor(null);


    }
}