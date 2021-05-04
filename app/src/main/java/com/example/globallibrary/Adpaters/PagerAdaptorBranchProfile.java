package com.example.globallibrary.Adpaters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.globallibrary.Fragment.BranchProfileFragment;
import com.example.globallibrary.Fragment.FragmentEditBranchProfile;
import com.example.globallibrary.Fragment.ProfileBranchDetailsFragment;

public class PagerAdaptorBranchProfile extends FragmentPagerAdapter {
    String BranchName;


    public PagerAdaptorBranchProfile(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return ProfileBranchDetailsFragment.newInstance(BranchProfileFragment.kuhaName());
            case 1:
                return new FragmentEditBranchProfile().newInstance(BranchProfileFragment.kuhaName());
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
                return "Branch Details";
            case 1:
                return "Edit Profile";
            default:
                return null;
        }
    }


}
