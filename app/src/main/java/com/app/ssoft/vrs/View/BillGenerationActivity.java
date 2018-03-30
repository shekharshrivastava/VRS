package com.app.ssoft.vrs.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ssoft.vrs.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BillGenerationActivity extends AppCompatActivity implements TextWatcher {

    private TextView tvDate;
    private long currentDateInMillis;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mDatabase;
    private String userId;
    private TextView tvBillNo;
    private EditText tvRateValue;
    private TextView tvAdvnAmnt;
    private EditText inputKm;
    private String advPayment;
    private TextView tvTotalAmnt;
    private TextView tvFinalAmnt;
    private String[] separated;
    private TextView ratePerType;
    private Button btnPay;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_generation);
        getSupportActionBar().setTitle("End Ride");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvDate = findViewById(R.id.tvDate);
        tvBillNo = findViewById(R.id.tvBillNo);
        tvRateValue = findViewById(R.id.rateValue);
        tvAdvnAmnt = findViewById(R.id.tvAdvnAmnt);
        inputKm = findViewById(R.id.inputKm);
        tvTotalAmnt = findViewById(R.id.tvTotalAmnt);
        tvFinalAmnt = findViewById(R.id.tvFinalAmnt);
        ratePerType = findViewById(R.id.ratePerType);
        btnPay = findViewById(R.id.btnPayFinal);

        inputKm.addTextChangedListener(this);
        Intent intent = getIntent();
        advPayment = intent.getStringExtra("advPayment");
        String rateValue = intent.getStringExtra("rateValue");
        final String userID = intent.getStringExtra("userIdVal");

        if (rateValue.contains("/")) {
            separated = rateValue.split("/");

            ; // this will contain "Fruit"
        }
        ratePerType.setText(separated[1]);
        tvRateValue.setText(separated[0]);
        tvAdvnAmnt.setText("Advance - " + advPayment);


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        tvDate.setText(formattedDate);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseInstance.getReference("BillingDetails");
        userId = mDatabase.push().getKey();
        tvBillNo.setText(userId);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child("vehicleDetails").child(userID).child("isVehBooked").setValue(false);
                database.child("vehicleDetails").child(userID).child("bookingDate").setValue("");
                Toast.makeText(BillGenerationActivity.this, "You have successfully ended your ride", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BillGenerationActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        try {
            int inputValue = Integer.parseInt(charSequence.toString());
            String totalAmnt = String.valueOf(inputValue * Integer.valueOf(separated[0]));
            tvTotalAmnt.setText("Total amount - " + totalAmnt);
            if (Integer.valueOf(advPayment) > Integer.valueOf(totalAmnt)) {
                tvFinalAmnt.setText("Return amount - " + String.valueOf(Integer.valueOf(advPayment) - Integer.valueOf(totalAmnt)));
            } else {
                tvFinalAmnt.setText("Final Amount - " + String.valueOf(Integer.valueOf(totalAmnt) - Integer.valueOf(advPayment)));
            }
        } catch (Exception ex) {

        }


    }

    @Override
    public void afterTextChanged(Editable editable) {

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
