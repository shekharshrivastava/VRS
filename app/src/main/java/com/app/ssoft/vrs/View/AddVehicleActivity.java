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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ivVehicalPhoto = findViewById(R.id.ivVehicalPhoto);
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

        mAuth = FirebaseAuth.getInstance();
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
                    rlSource.setVisibility(View.VISIBLE);
                    rlDest.setVisibility(View.VISIBLE);
                } else {
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


            @Override
            public void onClick(View view) {
                String ownerName = tvOwnerName.getText().toString();
                String vehModel = tvVehicalName.getText().toString();
                String source = tvSource.getText().toString();
                String destination = tvDestination.getText().toString();
                String rateValue = tvRateValue.getText().toString();
                String userId = mDatabase.push().getKey();
                String userLoginID = currentUser.getUid();
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
                if (!ownerName.isEmpty() && !vehModel.isEmpty() && !rateValue.isEmpty()) {
                    VehicleData vehicleData = new VehicleData(userLoginID, userId, vehType, ownerName, bitmapArray, vehModel, permit, routeValue, source, destination, rateValue, fuelType, seaterValue, driverRequired, driverName, driverNumber, driverAddress, driverLicence, driverAadhar, null);
                    mDatabase.child(userId).setValue(vehicleData);

                    Intent intent = new Intent();
                    intent.putExtra("dataAdded", true);
                    setResult(1, intent);
                    finish();//finishing activity
                } else {
                    Toast.makeText(AddVehicleActivity.this, "Please enter all required values", Toast.LENGTH_SHORT).show();
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
        ivVehicalPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
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

    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
