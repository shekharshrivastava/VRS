package com.app.ssoft.vrs.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.ssoft.vrs.Model.VehicleData;
import com.app.ssoft.vrs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ImageView ivVehicalPhoto;
    private String path = null;
    private String bitmapArray;
    private RadioGroup radionGroupRateType;
    private RadioButton radioButtonKM;
    private RadioButton radioButtonDay;
    private String rateType = "/DAY";
    private ImageView ivDriverPhoto;
    private String bitmapDriverArray;
    private RelativeLayout seatsRL;
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private String[] separated;
    private RelativeLayout fuelTypeRL;
    private RelativeLayout driverRL;
    private EditText advanceAmnt;
    private boolean isRouteFixed = true;
    private String rateFinalValue;
    private EditText tvOwnerNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fuelTypeRL = findViewById(R.id.fuelTypeRL);
        driverRL = findViewById(R.id.driverRL);
        seatsRL = findViewById(R.id.seatsRL);
        ivVehicalPhoto = findViewById(R.id.ivVehicalPhoto);
        ivDriverPhoto = findViewById(R.id.ivDriverPhoto);
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
        radioButtonDay = findViewById(R.id.rbHr);
        radioButtonKM = findViewById(R.id.rbKM);
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
        tvOwnerNumber = findViewById(R.id.tvOwnerNumber);
        radionGroupRateType = findViewById(R.id.radioGroupRate);
        advanceAmnt = findViewById(R.id.advanceAmnt);
        Intent intent = getIntent();
        final String userIdValue = intent.getStringExtra("userId");


        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Add Vehicles");
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
                permit = String.valueOf(tvPermitValue.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tvRouteValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                routeValue = String.valueOf(tvRouteValue.getSelectedItem());
                if (routeValue.equals("Fixed")) {
                    isRouteFixed = true;
                    rlSource.setVisibility(View.VISIBLE);
                    rlDest.setVisibility(View.VISIBLE);
                    advanceAmnt.setEnabled(false);
                    radioButtonKM.setEnabled(false);
                    radioButtonDay.setEnabled(false);
                } else {
                    isRouteFixed = false;
                    rlSource.setVisibility(View.GONE);
                    rlDest.setVisibility(View.GONE);
                    advanceAmnt.setEnabled(true);
                    radioButtonKM.setEnabled(true);
                    radioButtonDay.setEnabled(true);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tvSeaterValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seaterValue = String.valueOf(tvSeaterValue.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mFirebaseInstance = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = mFirebaseInstance.getReference("vehicleDetails");
        btnSubmit.setOnClickListener(new View.OnClickListener() {


            public String userId;

            @Override
            public void onClick(View view) {
                String ownerName = tvOwnerName.getText().toString();
                String vehModel = tvVehicalName.getText().toString();
                String source = tvSource.getText().toString();
                String destination = tvDestination.getText().toString();
                String rateValue = tvRateValue.getText().toString();
                String advanceAmntValue = advanceAmnt.getText().toString();
                String ownerNumber = tvOwnerNumber.getText().toString();
                if (!isRouteFixed) {
                    rateFinalValue = rateValue + rateType;
                } else {
                    rateFinalValue = rateValue;
                }

                if (driver_cv.getVisibility() == View.VISIBLE) {
                    driverName = tvDriverName.getText().toString();
                    driverNumber = tvDriverNumber.getText().toString();
                    driverAddress = tvDriverAddress.getText().toString();
                    driverLicence = tvDriverLicence.getText().toString();
                    driverAadhar = tvDriverAadhar.getText().toString();
                } else {
                    driverName = null;
                    driverNumber = null;
                    driverAadhar = null;
                    driverLicence = null;
                    driverAddress = null;
                }
                if (userIdValue == null) {
                    userId = mDatabase.push().getKey();
                    String userLoginID = currentUser.getUid();
                    if (!ownerName.isEmpty() && !vehModel.isEmpty() && !rateValue.isEmpty()  && (!ownerNumber.isEmpty() && ownerNumber.length() == 10)) {
                        VehicleData vehicleData = new VehicleData(userId, vehType, ownerName, bitmapArray, vehModel, permit, routeValue, source, destination, rateFinalValue, fuelType, seaterValue, driverRequired, driverName, driverNumber, driverAddress, driverLicence, driverAadhar, bitmapDriverArray, userLoginID, false, null, null, advanceAmntValue, ownerNumber);
                        mDatabase.child(userId).setValue(vehicleData);

                        Intent intent = new Intent();
                        intent.putExtra("dataAdded", true);
                        setResult(1, intent);
                        finish();//finishing activity
                    } else {
                        Toast.makeText(AddVehicleActivity.this, "Please enter all required values", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!ownerName.isEmpty() && !vehModel.isEmpty() && !rateValue.isEmpty() && !advanceAmntValue.isEmpty() && (!ownerNumber.isEmpty() && ownerNumber.length() == 10)) {
                        database.child("vehicleDetails").child(userIdValue).child("vehicleType").setValue(vehType);
                        database.child("vehicleDetails").child(userIdValue).child("ownerName").setValue(ownerName);
                        database.child("vehicleDetails").child(userIdValue).child("vehiclePhoto").setValue(bitmapArray);
                        database.child("vehicleDetails").child(userIdValue).child("vehicleModel").setValue(vehModel);
                        database.child("vehicleDetails").child(userIdValue).child("aip").setValue(permit);
                        database.child("vehicleDetails").child(userIdValue).child("routeType").setValue(routeValue);
                        database.child("vehicleDetails").child(userIdValue).child("source").setValue(source);
                        database.child("vehicleDetails").child(userIdValue).child("destination").setValue(destination);
                        database.child("vehicleDetails").child(userIdValue).child("rateValue").setValue(rateValue + rateType);
                        database.child("vehicleDetails").child(userIdValue).child("fuelType").setValue(fuelType);
                        database.child("vehicleDetails").child(userIdValue).child("numberOfseat").setValue(seaterValue);
                        database.child("vehicleDetails").child(userIdValue).child("driverReq").setValue(driverRequired);
                        database.child("vehicleDetails").child(userIdValue).child("driverName").setValue(driverName);
                        database.child("vehicleDetails").child(userIdValue).child("driverNumber").setValue(driverNumber);
                        database.child("vehicleDetails").child(userIdValue).child("driverAddress").setValue(driverAddress);
                        database.child("vehicleDetails").child(userIdValue).child("licenceNumber").setValue(driverLicence);
                        database.child("vehicleDetails").child(userIdValue).child("aadharNumber").setValue(driverAadhar);
                        database.child("vehicleDetails").child(userIdValue).child("driverPhoto").setValue(bitmapDriverArray);
                        database.child("vehicleDetails").child(userIdValue).child("advanceAmnt").setValue(advanceAmntValue);
                        database.child("vehicleDetails").child(userIdValue).child("ownerNumber").setValue(ownerNumber);

                        Intent intent = new Intent();
                        intent.putExtra("dataAdded", true);
                        setResult(1, intent);
                        finish();//finishing activity
                    }
                }
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tvVehTypeBike:
                        vehType = (String) tvVehTypeBike.getText();
                        seatsRL.setVisibility(View.GONE);
                        driverRL.setVisibility(View.GONE);
                        tvFuelTypePetrol.setSelected(true);
                        tvFuelTypeDiesel.setEnabled(false);
                        radioButtonKM.setEnabled(false);
                        tvRouteValue.setSelection(1);
                        tvRouteValue.setEnabled(false);
                        seaterValue = "2";
                        break;
                    case R.id.tvVehTypeCar:
                        tvRouteValue.setEnabled(true);
                        vehType = (String) tvVehTypeCar.getText();
                        seatsRL.setVisibility(View.VISIBLE);
                        driverRL.setVisibility(View.VISIBLE);
                        tvFuelTypeDiesel.setEnabled(true);
                        radioButtonKM.setEnabled(true);
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
        ivVehicalPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        ivDriverPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDriverImage();
            }
        });

        radionGroupRateType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbHr:
                        rateType = (String) radioButtonDay.getText();
                        break;
                    case R.id.rbKM:
                        rateType = (String) radioButtonKM.getText();
                        break;

                }
            }
        });

        if (userIdValue != null) {
            updateVehicleDetails(userIdValue);
        }
    }

    private void selectImage() {


        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(AddVehicleActivity.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(AddVehicleActivity.this, getApplicationContext().getPackageName() + ".my.package.name.provider", f));

                    startActivityForResult(intent, 1);

                } else if (options[item].equals("Choose from Gallery"))

                {

                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);


                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    private void selectDriverImage() {


        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(AddVehicleActivity.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(AddVehicleActivity.this, getApplicationContext().getPackageName() + ".my.package.name.provider", f));

                    startActivityForResult(intent, 3);

                } else if (options[item].equals("Choose from Gallery"))

                {

                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 4);


                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("temp.jpg")) {

                        f = temp;

                        break;

                    }

                }

                try {

                    Bitmap bitmap;

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();


                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),

                            bitmapOptions);


                    ivVehicalPhoto.setImageBitmap(bitmap);

                    bitmapArray = BitMapToString(bitmap);

                    path = android.os.Environment

                            .getExternalStorageDirectory()

                            + File.separator

                            + "Phoenix" + File.separator + "default" + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg";

                    f.delete();

                    OutputStream outFile = null;

                    File file = new File(path);

                    try {

                        outFile = new FileOutputStream(file);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);

                        outFile.flush();

                        outFile.close();

                    } catch (FileNotFoundException e) {

                        e.printStackTrace();

                    } catch (IOException e) {

                        e.printStackTrace();

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

            } else if (requestCode == 2) {


                Uri selectedImage = data.getData();

                String[] filePath = {MediaStore.Images.Media.DATA};

                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);

                c.moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);

                path = c.getString(columnIndex);

                c.close();

                Bitmap thumbnail = (BitmapFactory.decodeFile(path));

                Log.w("path of image from gal.", path + "");

                ivVehicalPhoto.setImageBitmap(thumbnail);
                bitmapArray = BitMapToString(thumbnail);

            }

        }

        if (requestCode == 3) {

            File f = new File(Environment.getExternalStorageDirectory().toString());

            for (File temp : f.listFiles()) {

                if (temp.getName().equals("temp.jpg")) {

                    f = temp;

                    break;

                }

            }

            try {

                Bitmap bitmap;

                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();


                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),

                        bitmapOptions);


                ivDriverPhoto.setImageBitmap(bitmap);

                bitmapDriverArray = BitMapToString(bitmap);

                path = android.os.Environment

                        .getExternalStorageDirectory()

                        + File.separator

                        + "Phoenix" + File.separator + "default" + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg";

                f.delete();

                OutputStream outFile = null;

                File file = new File(path);

                try {

                    outFile = new FileOutputStream(file);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);

                    outFile.flush();

                    outFile.close();

                } catch (FileNotFoundException e) {

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();

                } catch (Exception e) {

                    e.printStackTrace();

                }

            } catch (Exception e) {

                e.printStackTrace();

            }

        } else if (requestCode == 4) {


            Uri selectedImage = data.getData();

            String[] filePath = {MediaStore.Images.Media.DATA};

            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);

            c.moveToFirst();

            int columnIndex = c.getColumnIndex(filePath[0]);

            path = c.getString(columnIndex);

            c.close();

            Bitmap thumbnail = (BitmapFactory.decodeFile(path));

            Log.w("path of image from gal.", path + "");

            ivDriverPhoto.setImageBitmap(thumbnail);
            bitmapDriverArray = BitMapToString(thumbnail);

        }

    }


    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
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

    public void updateVehicleDetails(String userId) {
        getSupportActionBar().setTitle("Update Details");
        btnSubmit.setText("Update");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("vehicleDetails").child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                VehicleData vehiclesData = dataSnapshot.getValue(VehicleData.class);
                if (vehiclesData != null) {
                    tvOwnerName.setText(vehiclesData.getOwnerName());
                    tvVehicalName.setText(vehiclesData.getVehicleModel());
                    tvOwnerNumber.setText(vehiclesData.getOwnerNumber());
                    String CurrentString = vehiclesData.getRateValue();
                    if (CurrentString.contains("/")) {
                        separated = CurrentString.split("/");

                        ; // this will contain "Fruit"

                        tvRateValue.setText(separated[0]);
                        if (separated[1].equals("KM")) {
                            radioButtonKM.setChecked(true);
                        } else {
                            radioButtonDay.setChecked(true);
                        }
                    }
                    if (vehiclesData.getVehiclePhoto() != null) {
                        bitmapArray = vehiclesData.getVehiclePhoto();
                        ivVehicalPhoto.setImageBitmap(StringToBitMap(vehiclesData.getVehiclePhoto()));
                    }
                    if (vehiclesData.getDestination() != null && vehiclesData.getSource() != null) {
                        tvDestination.setText(vehiclesData.getDestination());
                        tvSource.setText(vehiclesData.getSource());
                    }
                    if (vehiclesData.getFuelType().equals("Petrol")) {
                        tvFuelTypePetrol.setChecked(true);
                    } else {
                        tvFuelTypeDiesel.setChecked(true);
                    }
                    if (vehiclesData.getVehicleType().equals("Car")) {
                        tvVehTypeCar.setChecked(true);
                        if (vehiclesData.getNumberOfseat().equals("5")) {
                            tvSeaterValue.setSelection(0);
                        } else if (vehiclesData.getNumberOfseat().equals("6")) {
                            tvSeaterValue.setSelection(1);
                        } else {
                            tvSeaterValue.setSelection(2);
                        }
                    } else {
                        tvVehTypeBike.setChecked(true);
                        tvSeaterValue.setVisibility(View.GONE);
                        seaterValue = "2";
                    }
                    if(vehiclesData.getRouteType().equalsIgnoreCase("Variable")){
                        tvRouteValue.setSelection(1);
                    }else{
                        tvRouteValue.setSelection(0);
                    }

                  /*  tvFuelType.setText(vehiclesData.getFuelType());
                    tvSeaterValue.setText(vehiclesData.getNumberOfseat());*/
                    String isDriverAvailable = vehiclesData.getDriverReq();
                    if (isDriverAvailable != null && isDriverAvailable.equals("Yes")) {
                        if (vehiclesData.getDriverPhoto() != null) {
                            bitmapArray = vehiclesData.getDriverPhoto();
                            ivDriverPhoto.setImageBitmap(StringToBitMap(vehiclesData.getDriverPhoto()));
                        }
                        radioButtonYes.setChecked(true);
                        driver_cv.setVisibility(View.VISIBLE);
                        tvDriverName.setText(vehiclesData.getDriverName());
                        tvDriverNumber.setText(vehiclesData.getDriverNumber());
                    } else {
                        radioButtonNo.setChecked(true);
                        driver_cv.setVisibility(View.GONE);
                    }
                    advanceAmnt.setText(vehiclesData.getAdvanceAmnt());
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

}

