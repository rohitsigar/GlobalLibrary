package com.example.globallibrary.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.globallibrary.Activity.OtpVerify;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StudentRegistrationFragment extends Fragment {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    TextInputEditText fullName;
    TextInputEditText contactNumber;
    TextInputEditText emailAddress;
    TextInputEditText residentialAddress;
    TextInputEditText Dob;
    AutoCompleteTextView branchName;
    TextInputEditText passward;
    TextInputEditText reWritePassward;
    MaterialButton nextButton;
    final Calendar myCalendar = Calendar.getInstance();
    boolean test = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        branchName = view.findViewById(R.id.drop_down_bar_of_branch_name);
        fullName = view.findViewById(R.id.student_fullname);
        contactNumber = view.findViewById(R.id.student_contact_number);
        emailAddress = view.findViewById(R.id.student_email);
        residentialAddress = view.findViewById(R.id.student_address);
        Dob = view.findViewById(R.id.student_dob);
        List<String> list = new ArrayList<>();
        reWritePassward = view.findViewById(R.id.student_re_enter_passward);
        passward = view.findViewById(R.id.student_passward1);
        nextButton = view.findViewById(R.id.next);




        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        Dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        firestore.collection("/Branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            private static final String TAG = "Rohit";

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getString("BranchName"));
                    }
                    Log.d(TAG, list.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        ArrayAdapter<String> adaptor = new ArrayAdapter<String>(getContext(), R.layout.drop_down_item_branch_name_with_address,list);
        branchName.setAdapter(adaptor);
        branchName.setDropDownBackgroundDrawable(ContextCompat.getDrawable(getContext() , R.drawable.newbar_background));
        branchName.setDropDownHeight(500);

        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String s = Dob.getText().toString();
                String phoneNumber =  contactNumber.getText().toString();
                if(fullName.getText().toString().isEmpty())
                {
                    fullName.setError("Please Enter Your Name");
                }
                else if(contactNumber.getText().toString().isEmpty())
                {
                    contactNumber.setError("Please Enter Your Contact Number");
                }
                else if(emailAddress.getText().toString().isEmpty())
                {
                    emailAddress.setError("Please Enter Your Email Address");
                }
                else if(residentialAddress.getText().toString().isEmpty())
                {
                    residentialAddress.setError("Please Enter Your Residential Address");
                }
                else if(Dob.getText().toString().isEmpty())
                {
                    fullName.setError("Please Enter Your Date OF birth");
                }
                else if(branchName.getText().toString().isEmpty())
                {
                    branchName.setError("Please Select Your Branch");
                }
                else if(passward.getText().toString().isEmpty())
                {
                    passward.setError("Please Enter Your Passward");
                }
                else if(reWritePassward.getText().toString().isEmpty())
                {
                    fullName.setError("Please Re - Write Your Passward");
                }
                else if(s.charAt(4)!='/'||s.charAt(7)!='/')
                {
                    Dob.setError("Please Enter Your Date of Birth in yyyy/mm/dd format");
                }
                else if(passward.getText().toString().equals(reWritePassward.getText().toString())) {
                    firestore.collection("Branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                for(QueryDocumentSnapshot document1 : task.getResult())
                                {
                                    if(document1.getString("BranchName").equals(branchName.getText().toString()))
                                    {
                                        firestore.collection("Branches").document(document1.getId()).collection("StudentDetails").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            private static final String TAG = "Rohit";

                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        if (contactNumber.getText().toString().equals(document.getString("ContactNumber").toString())) {
                                                            contactNumber.setError("This Contact Number is already Registered in this Library");
                                                            test = false;
                                                        }
                                                    }
                                                    if(test)
                                                    {
                                                        Log.d("hello", "onComplete: ");
                                                        Intent intent = new Intent(getActivity(), OtpVerify.class);
                                                        intent.putExtra("phoneNo", contactNumber.getText().toString().trim());
                                                        intent.putExtra("fullName", fullName.getText().toString().trim());
                                                        intent.putExtra("email", emailAddress.getText().toString().trim());
                                                        intent.putExtra("address", residentialAddress.getText().toString().trim());
                                                        intent.putExtra("dob", Dob.getText().toString().trim());
                                                        intent.putExtra("branch", branchName.getText().toString().trim());
                                                        intent.putExtra("passward", passward.getText().toString().trim());
                                                        intent.putExtra("userName", passward.getText().toString().trim());
                                                        startActivity(intent);

                                                    }

                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });

                }
                else
                {
                    reWritePassward.setError("Passward does not Match");
                }
            }
        });

    }
    private void updateLabel() {
        String myFormat = "yyyy/mm/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Dob.setText(sdf.format(myCalendar.getTime()));
    }

}