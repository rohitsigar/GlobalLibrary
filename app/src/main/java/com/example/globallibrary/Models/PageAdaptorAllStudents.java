package com.example.globallibrary.Models;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.globallibrary.Fragment.AllStudentsAttandanceFragment;
import com.example.globallibrary.Fragment.AllStudentsGridFragment;
import com.example.globallibrary.Fragment.BranchStudentFragment;



public class PageAdaptorAllStudents extends FragmentPagerAdapter {
    String BranchName;


    public PageAdaptorAllStudents(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return AllStudentsGridFragment.newInstance(BranchStudentFragment.kuhaName());
            case 1:
                return new AllStudentsAttandanceFragment().newInstance(BranchStudentFragment.kuhaName());
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
                return "All";
            case 1:
                return "Attandance";
            default:
                return null;
        }
    }


}
