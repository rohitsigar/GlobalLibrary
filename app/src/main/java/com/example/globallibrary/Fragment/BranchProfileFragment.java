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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.globallibrary.Activity.GeneralActivity;
import com.example.globallibrary.Adpaters.PagerAdaptorBranchProfile;
import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class BranchProfileFragment extends Fragment {
    private static String BranchId= "";
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdaptorBranchProfile viewPagerAdapter;
    CircleImageView BranchImage;
    ImageView ChangeImage;
    ImageView UploadImage;
    TextView BranchName;
    String URL  = "";
    private ProgressBar mProgressBar;
    private Uri filePath;

    EditText LibraryAddress , Disreption , OwnerName , EmailAddress;
    MaterialButton TickButton;

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
        return inflater.inflate(R.layout.fragment_branch_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

  UploadImage = view.findViewById(R.id.change_image_button);


        BranchImage = view.findViewById(R.id.id_Profile_Image);
        ChangeImage = view.findViewById(R.id.change_upload);
        BranchName  = view.findViewById(R.id.id_fullName_TextView);
//        Discreption  = view.findViewById(R.id.branch_discreption);
        BranchId = getArguments().getString("branchId");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        DocumentReference docIdRef = firebaseFirestore.collection("Branches/" ).document(BranchId);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                      URL = "Branches/"  + document.getString("BranchName");
                      BranchName.setText(document.getString("BranchName"));
                        storageReference.child(URL).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL for 'users/me/profile.png'
//                Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                                /// The string(file link) that you need
//
                                Uri downloadUrl = uri;
                                String s = downloadUrl.toString();
                                Log.d("hello guys", "onSuccess: " + s);
                                Picasso.get().load(s).into(BranchImage);


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors


                            }
                        });


                    } else {

                    }
                } else {

                }
            }
        });

        TickButton = view.findViewById(R.id.tick_button_branch);
        LibraryAddress = view.findViewById(R.id.change_owner_library_address);
        OwnerName = view.findViewById(R.id.change_owner_name);
        Disreption = view.findViewById(R.id.change_discreption);
        EmailAddress = view.findViewById(R.id.change_email_id);
        BranchId = getArguments().getString("branchId");
        Log.d("TAG", "onViewCreated: checking" + BranchId);


        firebaseFirestore.collection("Branches/" ).document(BranchId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                        OwnerName.setText(document.getString("OwnerName"));
                        Disreption.setText(document.getString("Discreption"));
                        EmailAddress.setText(document.getString("EmailAddress"));
                        LibraryAddress.setText(document.getString("LibraryAddress"));

                    } else {

                    }
                } else {

                }
            }
        });

        TickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference docIdRef = firebaseFirestore.collection("Branches/" ).document(BranchId);
                docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                if(!LibraryAddress.getText().toString().isEmpty())
                                {
                                    firebaseFirestore.collection("/Branches").document(document.getId()).update("LibraryAddress" , LibraryAddress.getText().toString());
                                }
                                if(!OwnerName.getText().toString().isEmpty())
                                {
                                    firebaseFirestore.collection("/Branches").document(document.getId()).update("OwnerName" , OwnerName.getText().toString());
                                }
                                if(!Disreption.getText().toString().isEmpty())
                                {
                                    firebaseFirestore.collection("/Branches").document(document.getId()).update("Discreption" , Disreption.getText().toString());
                                }

                                if(!EmailAddress.getText().toString().isEmpty())
                                {
                                    firebaseFirestore.collection("/Branches").document(document.getId()).update("EmailAddress" , EmailAddress.getText().toString());
                                }



                                Toast.makeText(getActivity() , "Profile Updated" , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity() , GeneralActivity.class);
                                startActivity(intent);





                            } else {

                            }
                        } else {

                        }
                    }
                });




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
    public static String kuhaName()
    {
        return BranchId;
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
                BranchImage.setImageBitmap(bitmap);
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