package com.example.globallibrary.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.globallibrary.R;

public class StudentSettingFragment extends Fragment {

    TextView Passward;
    TextView ContactNumber;
    String StudentId;
    String BranchId;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StudentId = getArguments().getString("StudentId");
        BranchId = getArguments().getString("BranchId");

        Passward = view.findViewById(R.id.change_passward);
        ContactNumber = view.findViewById(R.id.change_phone_no);
        getActivity().findViewById(R.id.return_back111_backup).setVisibility(View.GONE);
        getActivity().findViewById(R.id.return_back111).setVisibility(View.VISIBLE);








        Passward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ChangePasswardStudentFragment();
                Bundle bundle = new Bundle();
                bundle.putString("StudentId" , StudentId);
                bundle.putString("BranchId" , BranchId);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_setting, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        ContactNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ChangePhoneNoStudentFragment();
                Bundle bundle = new Bundle();
                bundle.putString("StudentId" , StudentId);
                bundle.putString("BranchId" , BranchId);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_setting, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }


}