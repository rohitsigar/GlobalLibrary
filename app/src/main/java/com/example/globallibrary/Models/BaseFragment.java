package com.example.globallibrary.Models;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    private boolean mShowingChild;
    protected PageFragmentListener mListener;
    public boolean isShowingChild() {
        return mShowingChild;
    }
    protected void setShowingChild(boolean showingChild) {
        mShowingChild = showingChild;
    }
}