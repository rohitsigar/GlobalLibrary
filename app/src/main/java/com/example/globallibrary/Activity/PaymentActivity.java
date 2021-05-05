package com.example.globallibrary.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.globallibrary.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    String UniqueId;
    String Amount;
    String StudentName;
    String StudentId;
    String BranchId;
    String EmailAddress;
    String ContactNumber;


    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Checkout.preload(getApplicationContext());
        Intent intent =getIntent();
        UniqueId = intent.getStringExtra("UniqueId");
        Amount = intent.getStringExtra("Amount");
        StudentName = intent.getStringExtra("StudentName");
        StudentId = intent.getStringExtra("StudentId");
        BranchId = intent.getStringExtra("BranchId");
        EmailAddress = intent.getStringExtra("EmailAddress");
        ContactNumber = intent.getStringExtra("ContactNumber");


        Checkout checkout = new Checkout();
        checkout.setKeyID("ENTER_RAZER_PAY_KEY_HERE");
        /**
         * Instantiate Checkout
         */


        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.app_logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", StudentName);
            options.put("description", "Reference No : " + UniqueId);
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", Amount);//pass amount in currency subunits
            options.put("prefill.email", EmailAddress);
            options.put("prefill.contact",ContactNumber);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        firebaseFirestore.collection("Branches").document(BranchId).collection("Fee").document(UniqueId).update("Status" , true);
        firebaseFirestore.collection("Branches").document(BranchId).collection("StudentDetails").document(StudentId).collection("Fee").document(UniqueId).update("Status" , true);
        Toast.makeText(PaymentActivity.this , "Payment is Sucessfully done", Toast.LENGTH_LONG).show();
        Intent i  = new Intent(PaymentActivity.this , GeneralActivity.class);
        startActivity(i);


    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(PaymentActivity.this , "Payment is Not Please Try Again leter", Toast.LENGTH_LONG).show();



    }
}
