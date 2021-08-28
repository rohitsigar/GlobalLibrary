package com.example.globallibrary.Fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.globallibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class HomeBranchFragment extends Fragment {

    MaterialButton Quote;
    AlertDialog alertDialog;
    EditText QuoteText;
    MaterialButton SendQuote;
    FirebaseStorage storage;
    StorageReference storageReference;
    TextView SetQuote;

    TextView GenerateQrCode;

    MaterialButton Done;
    EditText OpeningHour , ClosingHour , OpeningMinute , ClosingMinute;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;


String branchId;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.home_branch_fragment, container, false);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        SetQuote = v.findViewById(R.id.set_quote);

        branchId = getArguments().getString("branchId");
        DocumentReference docIdRef = firebaseFirestore.collection("Branches/" ).document(branchId);

        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                             SetQuote.setText(document.getString("Quote"));


                    } else {

                    }
                } else {

                }
            }
        });


        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Quote = view.findViewById(R.id.Quote);
        branchId = getArguments().getString("branchId");
        GenerateQrCode = view.findViewById(R.id.mark_location);
        GenerateQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = getView().findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.add_location_branch_dilog, viewGroup, false);


                //Now we need an AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);

                //finally creating the alert dialog and displaying it
                alertDialog = builder.create();
                alertDialog.show();
                Done = alertDialog.findViewById(R.id.buttonOk);
                OpeningHour  = alertDialog.findViewById(R.id.openinig_hour);
                OpeningMinute = alertDialog.findViewById(R.id.openinig_minute);
                ClosingHour = alertDialog.findViewById(R.id.closing_hour);
                ClosingMinute  = alertDialog.findViewById(R.id.closing_minute);

                DocumentReference docIdRef1 = firebaseFirestore.collection("/Branches/" ).document(branchId.trim());
                docIdRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                if (document.contains("OpenHour"))
                                {
                                    OpeningHour.setText(String.valueOf(document.getDouble("OpenHour").intValue()));
                                    ClosingHour.setText(String.valueOf(document.getDouble("CloseHour").intValue()));
                                    ClosingMinute.setText(String.valueOf(document.getDouble("CloseMinute").intValue()));
                                    OpeningMinute.setText(String.valueOf(document.getDouble("OpenMinute").intValue()));

                                }
                                else
                                {
                                    OpeningHour.setText("0");
                                    ClosingHour.setText("0");
                                    ClosingMinute.setText("0");
                                    OpeningMinute.setText("0");

                                }


                            } else {
                            }
                        } else {

                        }
                    }
                });

                ProgressBar progressBar = alertDialog.findViewById(R.id.progressbar_location);
                Done.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Double OpenHour , OpenMinute , CloseHour , CloseMinute;
                        if(OpeningHour.getText().toString().isEmpty() || OpeningMinute.getText().toString().isEmpty() ||
                                ClosingMinute.getText().toString().isEmpty() || ClosingHour.getText().toString().isEmpty() )
                        {
                            Toast.makeText(getContext() , "Timings are not properly set!" , Toast.LENGTH_LONG).show();
                            alertDialog.dismiss();
                        }
                        OpenHour = Double.valueOf(OpeningHour.getText().toString().trim());
                        CloseHour = Double.valueOf(ClosingHour.getText().toString().trim());
                        CloseMinute = Double.valueOf(ClosingMinute.getText().toString().trim());
                        OpenMinute =Double.valueOf(OpeningMinute.getText().toString().trim());

                        WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);

                        // initializing a variable for default display.
                        Display display = manager.getDefaultDisplay();

                        // creating a variable for point which
                        // is to be displayed in QR Code.
                        Point point = new Point();
                        display.getSize(point);

                        // getting width and
                        // height of a point
                        int width = point.x;
                        int height = point.y;

                        // generating dimension from width and height.
                        int dimen = width < height ? width : height;
                        dimen = dimen * 3 / 4;

                        // setting this dimensions inside our qr code
                        // encoder to generate our qr code.
                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);
                        Date currentTime = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + currentTime.toString());

                        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        String formattedDate = df.format(c);





                        qrgEncoder = new QRGEncoder(currentTime.toString().trim(), null, QRGContents.Type.TEXT, dimen);
                        try {
                            // getting our qrcode in the form of bitmap.
                            bitmap = qrgEncoder.encodeAsBitmap();

                            Log.d("TAG", "onClick: checking" + bitmap.toString());
                            saveImage(bitmap);
                            firebaseFirestore.collection("Branches").document(branchId).update("UniqueQrCode" , currentTime.toString().trim());
                            firebaseFirestore.collection("Branches").document(branchId).update("OpenHour" , OpenHour);
                            firebaseFirestore.collection("Branches").document(branchId).update("CloseHour" , CloseHour);
                            firebaseFirestore.collection("Branches").document(branchId).update("OpenMinute" , OpenMinute);
                            firebaseFirestore.collection("Branches").document(branchId).update("CloseMinute" , CloseMinute);


                            Toast.makeText(getContext() , "QR Code is saved in your gallery which can be used for student attandance" , Toast.LENGTH_LONG).show();
                            alertDialog.dismiss();
//                            saveTempBitmap(bitmap);
                            // the bitmap is set inside our image
                            // view using .setimagebitmap method.

                        } catch (WriterException e) {
                            // this method is called for
                            // exception handling.
                            Log.e("Tag", e.toString());
                        }










                    }
                });

            }
        });
        Log.d("TAG", "onViewCreated: checkhigh" + getArguments().getString("branchId").trim());

        Quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewGroup viewGroup = getView().findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dilog_set_quote, viewGroup, false);


                //Now we need an AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);

                //finally creating the alert dialog and displaying it
                alertDialog = builder.create();
                alertDialog.show();
                QuoteText = alertDialog.findViewById(R.id.quote);
                SendQuote = alertDialog.findViewById(R.id.sendQuote);
                SendQuote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.d("TAG", "onClick: check " + branchId);
                        DocumentReference docIdRef = firebaseFirestore.collection("Branches/" ).document(branchId);

                        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {

                                        if(QuoteText.getText().toString().isEmpty())
                                        {
                                            QuoteText.setError("Field Empty");
                                            Toast.makeText(getActivity() , "Please Enter Quote First!" , Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            SetQuote.setText(QuoteText.getText().toString());
                                            firebaseFirestore.collection("Branches").document(branchId.trim()).update("Quote" ,QuoteText.getText().toString() );
                                            Toast.makeText(getActivity() , "Quote is Send!" , Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                        }




                                    } else {

                                    }
                                } else {

                                }
                            }
                        });

                    }
                });



            }
        });

    }
    private void saveImage(Bitmap bitmap) {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            ContentValues values = contentValues();
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + getString(R.string.app_name));
            values.put(MediaStore.Images.Media.IS_PENDING, true);

            Uri uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try {
                    saveImageToStream(bitmap, getContext().getContentResolver().openOutputStream(uri));
                    values.put(MediaStore.Images.Media.IS_PENDING, false);
                    getContext().getContentResolver().update(uri, values, null, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } else {
            File directory = new File(Environment.getExternalStorageDirectory().toString() + '/' + getString(R.string.app_name));

            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".png";
            File file = new File(directory, fileName);
            try {
                saveImageToStream(bitmap, new FileOutputStream(file));
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private ContentValues contentValues() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        }
        return values;
    }

    private void saveImageToStream(Bitmap bitmap, OutputStream outputStream) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}