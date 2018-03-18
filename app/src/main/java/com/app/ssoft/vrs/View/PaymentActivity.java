package com.app.ssoft.vrs.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.ssoft.vrs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaymentActivity extends AppCompatActivity {

    private Button paymentBtn;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setTitle("Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = mFirebaseInstance.getReference("BookingDetails");
        Intent confirmationIntent = getIntent();
        final String firstName = confirmationIntent.getStringExtra("firstName");
        final String lastName = confirmationIntent.getStringExtra("lastNameVal");
        final String liceneceNumber = confirmationIntent.getStringExtra("licenceNumber");
        final String address = confirmationIntent.getStringExtra("addressVal");
        final String contactNumber = confirmationIntent.getStringExtra("contactNumberVal");
        final String dateSelected = confirmationIntent.getStringExtra("dateSelected");
        final String userID = confirmationIntent.getStringExtra("userIdVal");
        final String userKey = mDatabase.push().getKey();
        paymentBtn = findViewById(R.id.paymentBtn);

            paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BookRideData bookRideData = new BookRideData(userKey, firstName, lastName, liceneceNumber,BookVehicleActivity.bitmapArray,address,contactNumber,currentUser.getEmail(),"1250",true,null,null,null,dateSelected);
                    mDatabase.child(userKey).setValue(bookRideData);
                    database.child("vehicleDetails").child(userID).child("isVehBooked").setValue(true);
                    database.child("vehicleDetails").child(userID).child("bookingDate").setValue(dateSelected);
                    database.child("vehicleDetails").child(userID).child("customerUserID").setValue(currentUser.getUid());
                    Toast.makeText(PaymentActivity.this, "Booking done successfully!", Toast.LENGTH_SHORT).show();


                } catch (Exception e) {
                    Toast.makeText(PaymentActivity.this, "Booking Failed !", Toast.LENGTH_SHORT).show();

                    e.printStackTrace();
                }
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
