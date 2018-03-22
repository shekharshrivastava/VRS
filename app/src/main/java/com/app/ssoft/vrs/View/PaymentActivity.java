package com.app.ssoft.vrs.View;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.ssoft.vrs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.intentfilter.androidpermissions.PermissionManager;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singleton;

public class PaymentActivity extends AppCompatActivity {

    private Button paymentBtn;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private Spinner cardTypeSpinner;
    private Spinner paymentTypeSpinner;
    private ArrayList<String> ownerNumber;
    private String ownerNumberValue;
    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setTitle("Payment");
        ownerNumber = new ArrayList<>();
        cardTypeSpinner = findViewById(R.id.cardTypeSpinner);
        paymentTypeSpinner = findViewById(R.id.paymentTypeSpinner);
        permissionManager = PermissionManager.getInstance(PaymentActivity.this);

        List<String> cardTypeList = new ArrayList<String>();
        cardTypeList.add("Visa");
        cardTypeList.add("Mastercard");
        cardTypeList.add("Rupay");
        ArrayAdapter<String> cardTypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, cardTypeList);
        cardTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cardTypeSpinner.setAdapter(cardTypeAdapter);

        List<String> paymentTypeList = new ArrayList<String>();
        paymentTypeList.add("Credit Card");
        paymentTypeList.add("Debit Card");
        ArrayAdapter<String> paymentTypeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, paymentTypeList);
        paymentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentTypeSpinner.setAdapter(paymentTypeAdapter);


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
        final String advnPayment = confirmationIntent.getStringExtra("advnPay");
        ownerNumberValue = confirmationIntent.getStringExtra("ownerNumber");
        ownerNumber.add(ownerNumberValue);

        final String userKey = mDatabase.push().getKey();
        paymentBtn = findViewById(R.id.paymentBtn);
        if(advnPayment!=null) {
            paymentBtn.setText("PAY " + advnPayment);
        }
            paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    permissionManager.checkPermissions(singleton(Manifest.permission.SEND_SMS), new PermissionManager.PermissionRequestListener() {
                        @Override
                        public void onPermissionGranted() {
                            BookRideData bookRideData = new BookRideData(userKey, firstName, lastName, liceneceNumber,BookVehicleActivity.bitmapArray,address,contactNumber,currentUser.getEmail(),"1250",true,null,null,null,dateSelected);
                            mDatabase.child(userKey).setValue(bookRideData);
                            database.child("vehicleDetails").child(userID).child("isVehBooked").setValue(true);
                            database.child("vehicleDetails").child(userID).child("bookingDate").setValue(dateSelected);
                            database.child("vehicleDetails").child(userID).child("customerUserID").setValue(currentUser.getUid());
                            Toast.makeText(PaymentActivity.this, "Booking done successfully!", Toast.LENGTH_SHORT).show();
                            if(ownerNumber.get(0) !=null) {
                                MultipleSMS(ownerNumber.get(0).toString(), "Congratulations! Your vehicle is booked");
                            }
                            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onPermissionDenied() {
                            Toast.makeText(PaymentActivity.this, "Required permission to access file manager, please provide permission ", Toast.LENGTH_SHORT).show();
                        }


                    });


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
    private void MultipleSMS(String phoneNumber, final String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        // ---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        ContentValues values = new ContentValues();
                        for (int i = 0; i < ownerNumber.size() - 1; i++) {
                            values.put("address", ownerNumber.get(i).toString());
                            // txtPhoneNo.getText().toString());
                            values.put("body",message );


                        }
                        getContentResolver().insert(
                                Uri.parse("content://sms/sent"), values);
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        // ---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }
}
