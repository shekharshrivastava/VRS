package com.app.ssoft.vrs.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.ssoft.vrs.Model.VehicleData;
import com.app.ssoft.vrs.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        boolean isFromMyVehList = intent.getBooleanExtra("isFromMyVehicles", false);
        if (!isFromMyVehList) {
            btnPay.setVisibility(View.GONE);
        }else{
            btnPay.setVisibility(View.VISIBLE);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference().child("vehicleDetails").child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                VehicleData vehiclesData = dataSnapshot.getValue(VehicleData.class);
                if (vehiclesData != null) {
                    tvOwnerName.setText(vehiclesData.getOwnerName());
                    tvVehicalName.setText(vehiclesData.getVehicleModel());
                    tvRateValue.setText(vehiclesData.getRateValue());
                    tvFuelType.setText(vehiclesData.getFuelType());


                    if (vehiclesData.getVehiclePhoto() != null) {
                        ivVehiclePhoto.setImageBitmap(StringToBitMap(vehiclesData.getVehiclePhoto()));
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
