package com.app.ssoft.vrs.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.ssoft.vrs.Model.VehicleData;
import com.app.ssoft.vrs.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddVehicleActivity extends AppCompatActivity {

    private Button btnSubmit;
    private DatabaseReference mDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private VehicleData vehicleData;
    private RadioGroup radioGroup;
    private RadioButton tvVehTypeCar;
    private String vehType = "Car";
    private RadioButton tvVehTypeBike;
    private EditText tvOwnerName;
    private EditText tvVehicalName;
    private EditText tvSource;
    private EditText tvDestination;
    private EditText tvRateValue;
    private RadioGroup radioGroupFuelType;
    private RadioButton tvFuelTypeDiesel;
    private RadioButton tvFuelTypePetrol;
    private String fuelType = "Petrol";
    private RadioGroup radioGroupDriverReq;
    private RadioButton radioButtonYes;
    private RadioButton radioButtonNo;
    private String driverRequired = "NO";
    private CardView driver_cv;
    private EditText tvDriverName;
    private EditText tvDriverNumber;
    private EditText tvDriverAddress;
    private EditText tvDriverLicence;
    private EditText tvDriverAadhar;
    private String driverName;
    private String driverNumber;
    private String driverLicence;
    private String driverAddress;
    private String driverAadhar;
    private Spinner tvPermitValue;
    private String permit;
    private Spinner tvRouteValue;
    private String routeValue;
    private Spinner tvSeaterValue;
    private String seaterValue;
    private RelativeLayout rlSource;
    private RelativeLayout rlDest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroupFuelType = findViewById(R.id.radioGroupFuel);
        radioGroupDriverReq = findViewById(R.id.radioGroupDriverType);
        tvVehTypeCar = findViewById(R.id.tvVehTypeCar);
        tvVehTypeBike = findViewById(R.id.tvVehTypeBike);
        tvOwnerName = findViewById(R.id.tvOwnerName);
        tvVehicalName = findViewById(R.id.tvVehicalName);
        tvSource = findViewById(R.id.tvSource);
        tvDestination = findViewById(R.id.tvDestValue);
        tvRateValue = findViewById(R.id.tvRateValue);
        tvFuelTypeDiesel = findViewById(R.id.tvFuelTypeDiesel);
        tvFuelTypePetrol = findViewById(R.id.tvFuelTypePetrol);
        radioButtonYes = findViewById(R.id.RbYes);
        radioButtonNo = findViewById(R.id.RbNo);
        btnSubmit = findViewById(R.id.btnSubmit);
        driver_cv = findViewById(R.id.driver_cv);
        tvDriverName = findViewById(R.id.tvDriverName);
        tvDriverNumber = findViewById(R.id.tvDriverNumber);
        tvDriverAddress = findViewById(R.id.tvDriverAddress);
        tvDriverLicence = findViewById(R.id.tvDriverLicence);
        tvDriverAadhar = findViewById(R.id.tvDriverAadhar);
        tvPermitValue = findViewById(R.id.tvPermitValue);
        tvRouteValue = findViewById(R.id.tvRouteValue);
        tvSeaterValue = findViewById(R.id.tvSeatsValue);
        rlSource = findViewById(R.id.rlSource);
        rlDest = findViewById(R.id.rlDest);

        List<String> permitList = new ArrayList<String>();
        permitList.add("Yes");
        permitList.add("No");

        List<String> routeList = new ArrayList<String>();
        routeList.add("Fixed");
        routeList.add("Variable");

        List<String> seaterList = new ArrayList<String>();
        seaterList.add("5");
        seaterList.add("6");
        seaterList.add("7");


        ArrayAdapter<String> permitAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, permitList);
        permitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tvPermitValue.setAdapter(permitAdapter);

        ArrayAdapter<String> routeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, routeList);
        routeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tvRouteValue.setAdapter(routeAdapter);

        ArrayAdapter<String> seaterAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, seaterList);
        seaterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tvSeaterValue.setAdapter(seaterAdapter);

        tvPermitValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                permit =String.valueOf(tvPermitValue.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tvRouteValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                routeValue =String.valueOf(tvRouteValue.getSelectedItem());
                if(routeValue.equals("Fixed")){
                    rlSource.setVisibility(View.VISIBLE);
                    rlDest.setVisibility(View.VISIBLE);
                }else{
                    rlSource.setVisibility(View.GONE);
                    rlDest.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tvSeaterValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seaterValue =String.valueOf(tvSeaterValue.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseInstance.getReference("vehicleDetails");
        btnSubmit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String ownerName = tvOwnerName.getText().toString();
                String vehModel = tvVehicalName.getText().toString();
                String source = tvSource.getText().toString();
                String destination = tvDestination.getText().toString();
                String rateValue = tvRateValue.getText().toString();
                String userId = mDatabase.push().getKey();
                if (driver_cv.getVisibility() == View.VISIBLE){
                     driverName = tvDriverName.getText().toString();
                     driverNumber = tvDriverNumber.getText().toString();
                     driverAddress = tvDriverAddress.getText().toString();
                     driverLicence = tvDriverLicence.getText().toString();
                     driverAadhar = tvDriverAadhar.getText().toString();
                }else{
                    driverName = null;
                    driverNumber = null;
                    driverAadhar = null;
                    driverLicence = null;
                    driverAddress = null;
                }
                if(!ownerName.isEmpty() && !vehModel.isEmpty()  && !rateValue.isEmpty()) {
                    VehicleData vehicleData = new VehicleData(vehType, ownerName, null, vehModel, permit, routeValue, source, destination, rateValue, fuelType, seaterValue, driverRequired, driverName, driverNumber, driverAddress, driverLicence, driverAadhar, null);
                    mDatabase.child(userId).setValue(vehicleData);

                    Intent intent = new Intent();
                    intent.putExtra("dataAdded", true);
                    setResult(1, intent);
                    finish();//finishing activity
                }else {
                    Toast.makeText(AddVehicleActivity.this,"Please enter all required values",Toast.LENGTH_SHORT).show();
                }
            }
        });




        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tvVehTypeBike:
                        vehType = (String) tvVehTypeBike.getText();
                        break;
                    case R.id.tvVehTypeCar:
                        vehType = (String) tvVehTypeCar.getText();
                        break;

                }
            }
        });

        radioGroupFuelType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.tvFuelTypeDiesel:
                        fuelType = (String) tvFuelTypeDiesel.getText();
                        break;
                    case R.id.tvVehTypeCar:
                        fuelType = (String) tvFuelTypePetrol.getText();
                        break;
                }
            }
        });
        radioGroupDriverReq.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.RbYes:
                        driver_cv.setVisibility(View.VISIBLE);
                        driverRequired = (String) radioButtonYes.getText();
                        break;
                    case R.id.RbNo:
                        driver_cv.setVisibility(View.GONE);
                        driverRequired = (String) radioButtonNo.getText();
                        break;
                }
            }
        });
    }


}
