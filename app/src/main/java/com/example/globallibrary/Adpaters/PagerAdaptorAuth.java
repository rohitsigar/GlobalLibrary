package com.example.globallibrary.Adpaters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.globallibrary.Fragment.AllBranchesFragment;
import com.example.globallibrary.Fragment.BranchOwnerLoginFragment;
import com.example.globallibrary.Fragment.LoginStudentFragment;

public class PagerAdaptorAuth extends FragmentPagerAdapter {

    public PagerAdaptorAuth(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LoginStudentFragment();
            case 1:
                return new BranchOwnerLoginFragment();
            case 2:
                return  new AllBranchesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Student";
            case 1:
                return "Branch";
            case 2:
                return  "All Branches";
            default:
                return null;
        }
    }
}
