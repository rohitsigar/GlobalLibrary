package com.example.globallibrary.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.globallibrary.Models.PageAdaptorAllStudents;
import com.example.globallibrary.R;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;


public class BranchStudentFragment extends Fragment {


    private static String BranchId;
    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdaptorAllStudents viewPagerAdapter;



    public BranchStudentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_branch_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BranchId  = getArguments().getString("branchId");

        tabLayout = view.findViewById(R.id.BranchStudentsTabLayout);
        viewPager = view.findViewById(R.id.BranchStudentsViewPager);
        viewPagerAdapter = new PageAdaptorAllStudents(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabRippleColor(null);
    }

    public static String kuhaName()
    {
        return BranchId;
    }
}