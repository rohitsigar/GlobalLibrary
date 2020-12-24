package com.example.globallibrary.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.globallibrary.R;

public class StudentRegistrationFragment extends Fragment {

private AutoCompleteTextView dropDown;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dropDown = view.findViewById(R.id.drop_down_bar_of_branch_name);
        String[] branch_details_for_drop_down = new String[]
                {
                        "branch 1",
                        "branch 2",
                        "branch 3",
                        "branch 4"
                };

        ArrayAdapter<String> adaptor = new ArrayAdapter<String>(getContext(), R.layout.drop_down_item_branch_name_with_address,branch_details_for_drop_down);
        dropDown.setAdapter(adaptor);
    }
}