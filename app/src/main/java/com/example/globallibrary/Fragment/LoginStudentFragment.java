package com.example.globallibrary.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.globallibrary.Activity.ForgetPasswardStudentActivity;
import com.example.globallibrary.Activity.GeneralActivity;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginStudentFragment extends Fragment {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    TextView signUpButton;
    TextInputEditText contactNumber;
    TextInputEditText passward;
    Button ForgetPassward;

    MaterialButton loginButton;
    boolean test = false;


    private static final String KEY_ACCESS = "access";
    private static final String SHARED_PREF = "PREF";
    private static final String KEY_STUDENT_ID  = "id_student";
    private static final  String KEY_STUDENT_B_ID  = "id_branch";


    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_login_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signUpButton = view.findViewById(R.id.sign_up_student);
        contactNumber = view.findViewById(R.id.student_phoneNo);
        passward = view.findViewById(R.id.student_password);
        ForgetPassward = view.findViewById(R.id.forget_password_student);
        ForgetPassward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , ForgetPasswardStudentActivity.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                getActivity().finish();
                startActivity(intent);

            }
        });
        loginButton = view.findViewById(R.id.go_button_student);
        sharedPreferences  = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        String loged  = sharedPreferences.getString(KEY_ACCESS , null);
        if(loged!=null)
        {
            Intent intent = new Intent(getActivity(), GeneralActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(contactNumber.getText().toString().trim().isEmpty())
                {
                    contactNumber.setError("This Filed is Empty");
                }
                else if(passward.getText().toString().trim().isEmpty())
                {
                    contactNumber.setError("This Field is Empty");
                }
                else
                {
                    firestore.collection("/Students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        private static final String TAG = "Rohit";

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String s = document.getString("ContactNumber");
                                    if(s.equals(contactNumber.getText().toString().trim()))
                                    {
                                        String p  = document.getString("Passward");
                                        if(p.equals(passward.getText().toString().trim()))
                                        {
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString(KEY_ACCESS , "StudentAccess");
                                            Log.d(TAG, "onComplete: checking" + contactNumber.getText().toString());
                                            String s1  = contactNumber.getText().toString();
                                            test = true;
                                            editor.putString(KEY_STUDENT_B_ID ,document.getString("BranchId").trim());
                                            editor.putString(KEY_STUDENT_ID ,document.getId().trim());


                                            editor.apply();
                                            Intent intent = new Intent(getActivity(), GeneralActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
//                                        Toast.makeText(getActivity(), "Login Sucessfully" , Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            passward.setError("Incorrect Passward");
                                            test = true;
                                        }
                                    }

                                }
                                if(!test) {
                                    contactNumber.setError("User Does not exist");
                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
                }


            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Bundle bundle = new Bundle();



                //create the new VendorFragment -> set Fragment Manager -> Transaction -> replace -> commit
//                android.app.Fragment fragment = new android.app.Fragment();
//
//
//
//                FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getFragmentManager();
//                fragmentManager.executePendingTransactions();
//                //Fragment currentFrag = fragmentManager.findFragmentById(c
//                //FragmentManager childFM = currentFrag.getChildFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.replace(R.id.container, fragment);
//
//
//                fragmentTransaction.commit();
                Fragment fragment = new StudentRegistrationFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_auth, fragment);
                fragmentTransaction.commit();
            }
        });
    }
}