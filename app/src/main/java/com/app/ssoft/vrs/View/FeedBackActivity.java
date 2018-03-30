package com.app.ssoft.vrs.View;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.ssoft.vrs.Model.FeedbackData;
import com.app.ssoft.vrs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FeedBackActivity extends AppCompatActivity {
    private EditText userName;
    private EditText bookingID;
    private EditText feedback;
    private Button submitBTN;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private ArrayList<String> ownerNumber;
    private String ownerNumberValue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("FeedBack");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.feedback_layout);
        userName = findViewById(R.id.userName);
        bookingID = findViewById(R.id.bookingID);
        feedback = findViewById(R.id.feedback);
        submitBTN = findViewById(R.id.submitBTN);
        ownerNumber = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();
        Intent intent = getIntent();
        ownerNumberValue = intent.getStringExtra("ownerNumber");
        if (ownerNumberValue != null && !ownerNumberValue.isEmpty()) {
            ownerNumber.add(ownerNumberValue);
        }

        mDatabase = mFirebaseInstance.getReference("Feedback");
        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userName.getText().toString();
                String bookingIDNo = bookingID.getText().toString();
                String feedBackValue = feedback.getText().toString();
                String userLoginID = currentUser.getEmail();
                String userId = mDatabase.push().getKey();
                if (!name.isEmpty() && !feedBackValue.isEmpty()) {
                    FeedbackData feedbackData = new FeedbackData(userLoginID, name, bookingIDNo, feedBackValue);
                    mDatabase.child(userId).setValue(feedbackData);
                    Toast.makeText(FeedBackActivity.this, "Thank you for your feedback", Toast.LENGTH_SHORT).show();
                    userName.setText("");
                    bookingID.setText("");
                    feedback.setText("");
                    if (ownerNumber.size() > 0 && ownerNumber.get(0) != null) {
                        MultipleSMS(ownerNumber.get(0).toString(), feedBackValue + "\n Regards " + "\n" + name + " \n" + userLoginID);
                    }
                    finish();
                } else {
                    Toast.makeText(FeedBackActivity.this, "Please enter all required values", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                            values.put("body", message);


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
