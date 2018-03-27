package com.app.ssoft.vrs.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ssoft.vrs.Model.VehicleData;
import com.app.ssoft.vrs.R;
import com.app.ssoft.vrs.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class VehicleDetailsActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView tvOwnerName;
    private TextView tvVehicalName;
    private TextView tvFuelType;
    private TextView tvSeatsValue;
    private RadioButton RbYes;
    private RadioButton RbNo;
    private TextView tvRateValue;
    private CardView driver_cv;
    private TextView tvDriverName;
    private TextView tvDriverNumber;
    private ImageView ivVehiclePhoto;
    private BitmapFactory.Options options;
    private ImageView ivDriverPhoto;
    private Button btnPay;
    private String userId;
    private String ownerUserId;
    private FirebaseAuth mAuth;
    private boolean isVehBooked = false;
    private long currentDateInMillis;
    private String advanceAmnt;
    private String rateValue;
    private String ownerNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_vehicle_details);
        tvOwnerName = findViewById(R.id.tvOwnerName);
        btnPay = findViewById(R.id.btnPay);
        ivVehiclePhoto = findViewById(R.id.ivVehicalPhoto);
        ivDriverPhoto = findViewById(R.id.ivDriverPhoto);
        tvVehicalName = findViewById(R.id.tvVehicalName);
        tvFuelType = findViewById(R.id.tvFuelType);
        tvSeatsValue = findViewById(R.id.tvSeatsValue);
        RbYes = findViewById(R.id.RbYes);
        RbNo = findViewById(R.id.RbNo);
        tvRateValue = findViewById(R.id.tvRateValue);
        driver_cv = findViewById(R.id.driver_cv);
        tvDriverName = findViewById(R.id.tvDriverName);
        tvDriverNumber = findViewById(R.id.tvDriverNumber);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy");
        String formattedDate = df.format(c);
        currentDateInMillis = Utils.getDateInMili(formattedDate);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        advanceAmnt = intent.getStringExtra("advanceAmnt");
        ownerNumber = intent.getStringExtra("ownerNumber");

        mAuth = FirebaseAuth.getInstance();
        boolean isFromMyVehList = intent.getBooleanExtra("isFromMyVehicles", false);
        if (!isFromMyVehList) {
            btnPay.setVisibility(View.GONE);
        } else {
            btnPay.setVisibility(View.VISIBLE);
        }
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent bookingIntent = new Intent(VehicleDetailsActivity.this, BookVehicleActivity.class);
                bookingIntent.putExtra("userID", userId);
                bookingIntent.putExtra("advPayment", advanceAmnt);
                bookingIntent.putExtra("rateValue", rateValue);
                bookingIntent.putExtra("ownerNumber", ownerNumber);
                startActivity(bookingIntent);
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("vehicleDetails").child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                VehicleData vehiclesData = dataSnapshot.getValue(VehicleData.class);
                if (vehiclesData != null) {
                    tvOwnerName.setText(vehiclesData.getOwnerName());
                    tvVehicalName.setText(vehiclesData.getVehicleModel());
                    rateValue = vehiclesData.getRateValue();
                    if (rateValue.contains("/")) {
                        tvRateValue.setText(rateValue);
                    } else {
                        tvRateValue.setText(rateValue + " Rs");
                    }
                    tvFuelType.setText(vehiclesData.getFuelType());
                    ownerUserId = vehiclesData.getCurrentUserID();
                    isVehBooked = vehiclesData.isVehBooked;

                    if ((vehiclesData.getBookingDate() != null && (!vehiclesData.getBookingDate().isEmpty() && Utils.getDateInMili(vehiclesData.getBookingDate()) >= currentDateInMillis))) {
                        btnPay.setEnabled(false);
                    } else {
                        btnPay.setEnabled(true);
                    }

                    if (vehiclesData.getVehiclePhoto() != null) {
                        ivVehiclePhoto.setImageBitmap(StringToBitMap(vehiclesData.getVehiclePhoto()));
                    }
                  /*  File imgFile = new File(vehiclesData.getVehiclePhoto());
                    if (imgFile.exists()) {
                        try {
                            Bitmap myBitmap = BitmapFactory.decodeFile(vehiclesData.getVehiclePhoto());
                            ivVehiclePhoto.setImageBitmap(myBitmap);
                        } catch (OutOfMemoryError e) {
                            try {
                                options = new BitmapFactory.Options();
                                options.inSampleSize = 2;
                                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                ivVehiclePhoto.setImageBitmap(bitmap);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }*/
                    tvSeatsValue.setText(vehiclesData.getNumberOfseat());
                    String isDriverAvailable = vehiclesData.getDriverReq();
                    if (isDriverAvailable != null && isDriverAvailable.equals("Yes")) {
                        if (vehiclesData.getDriverPhoto() != null) {
                            ivDriverPhoto.setImageBitmap(StringToBitMap(vehiclesData.getDriverPhoto()));
                        }
                        RbYes.setChecked(true);
                        driver_cv.setVisibility(View.VISIBLE);
                        tvDriverName.setText(vehiclesData.getDriverName());
                        tvDriverNumber.setText(vehiclesData.getDriverNumber());
                    } else {
                        RbNo.setChecked(true);
                        driver_cv.setVisibility(View.GONE);
                    }

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        MenuItem item = menu.findItem(R.id.action_edit);
        if (!mAuth.getCurrentUser().getUid().equals(ownerUserId)) {
            item.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                break;
            case R.id.action_edit: {
                Intent intent = new Intent(this, AddVehicleActivity.class);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, 1);
                return true;
            }
            case R.id.action_delete: {
                FirebaseDatabase.getInstance().getReference().child("vehicleDetails").child(userId).removeValue();
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_FIRST_USER) {
            boolean message = data.getBooleanExtra("dataAdded", false);
            Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

}
