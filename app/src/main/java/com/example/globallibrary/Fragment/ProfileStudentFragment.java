package com.example.globallibrary.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.globallibrary.Adpaters.PageAdaptorStudentProfile;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;


public class ProfileStudentFragment extends Fragment {

    private static String StudentName1 = "";
    private static String BranchName1 = "";
    private static String StudentId = "";
    private static String BranchId;
    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdaptorStudentProfile viewPagerAdapter;
    ImageView StudentImage;
    ImageView ChangeImage;
    ImageView UploadImage;
    TextView StudentName;
    TextView Discreption;
    String URL  = "";
    private ProgressBar mProgressBar;
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_student, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewPagerstudentProfile);
        viewPagerAdapter = new PageAdaptorStudentProfile(getChildFragmentManager()); //change krna hai
        UploadImage = view.findViewById(R.id.change_upload_student);
        viewPager.setAdapter(viewPagerAdapter); //change krna hai
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs_student_profile);
        tabLayout.setupWithViewPager(viewPager); //change krna hai
        tabLayout.setTabRippleColor(null);
        StudentImage = view.findViewById(R.id.id_Profile_Image_student);
        ChangeImage = view.findViewById(R.id.change_image_button_student);
        StudentName  = view.findViewById(R.id.id_fullName_TextView_student);
        Discreption  = view.findViewById(R.id.student_discreption);
                StudentId = getArguments().getString("StudentId");
                BranchId = getArguments().getString("BranchId");
        Log.d("TAG", "onViewCreated: StudentId " + StudentId + " BranchId "  + BranchId);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        DocumentReference docIdRef = firebaseFirestore.collection("/Branches/" + BranchId.trim() + "/StudentDetails" ).document(StudentId.trim());
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        StudentName.setText(document.getString("FullName"));
                                                                   Discreption.setText(document.getString("Discreption"));
                                                                    URL = "Student/" + StudentId;
                                                                   storageReference.child(URL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                       @Override
                                                                       public void onSuccess(Uri uri) {
                                                                           Uri downloadUrl = uri;
                                                                           String s = downloadUrl.toString();
                                                                           Log.d("hello guys", "onSuccess: " + s);
                                                                           Picasso.get().load(s).into(StudentImage);

                                                                       }
                                                                   }).addOnFailureListener(new OnFailureListener() {
                                                                       @Override
                                                                       public void onFailure(@NonNull Exception exception) {
                                                                           StudentImage.setImageResource(R.drawable.student_logo);
                                                                           // Handle any errors
                                                                       }
                                                                   });




                    } else {

                        Log.d("TAG", "onComplete: not possible" );

                    }
                } else {

                }
            }
        });
        ChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectImage();
                UploadImage.setVisibility(View.VISIBLE);
                ChangeImage.setVisibility(View.GONE);
            }
        });
        UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImage.setVisibility(View.GONE);
                ChangeImage.setVisibility(View.VISIBLE);
                uploadImage();
            }
        });



    }
    public static String[] kuhaName()
    {
        String[] s1  = new String[2];
        s1[0] = StudentId;
        s1[1] = BranchId;
        return s1;
    }
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);


    }
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getActivity().getApplicationContext().getContentResolver(),
                                filePath);
                StudentImage.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            Log.d("TAG", "uploadImage: going well ");

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            URL);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {



                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {


                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(getActivity(),
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getActivity(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }
}