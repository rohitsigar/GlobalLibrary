package com.example.globallibrary.Adpaters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.globallibrary.Fragment.FragmentEditStudentProfile;
import com.example.globallibrary.Fragment.ProfileStudentDetailsFragment;
import com.example.globallibrary.Fragment.ProfileStudentFragment;

public class PageAdaptorStudentProfile extends FragmentPagerAdapter {
    String BranchName;


    public PageAdaptorStudentProfile(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ProfileStudentDetailsFragment.newInstance(ProfileStudentFragment.kuhaName());
            case 1:
                return new FragmentEditStudentProfile().newInstance(ProfileStudentFragment.kuhaName());
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Student Details";
            case 1:
                return "Edit Profile";
            default:
                return null;
        }
    }


}
