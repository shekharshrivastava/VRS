package com.app.ssoft.vrs.View;

import android.app.DatePickerDialog;
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
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ssoft.vrs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

public class BookVehicleActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText licenceNum;
    private TextView attachDoc;
    private EditText address;
    private EditText contactNumber;
    private Button btnProceed;
    private TextView selectDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    private String userId;
    private EditText email;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String path;
    public static String bitmapArray = "";
    private String advancePayment;
    private EditText advPayment;
    private String rateValue;
    private String ownerNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_vehicle);
        mAuth = FirebaseAuth.getInstance();
        final Intent intent = getIntent();
        userId = intent.getStringExtra("userID");
        advancePayment = intent.getStringExtra("advPayment");
        rateValue = intent.getStringExtra("rateValue");
        ownerNumber = intent.getStringExtra("ownerNumber");
        getSupportActionBar().setTitle("Book your ride");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        licenceNum = findViewById(R.id.LicenceNum);
        attachDoc = findViewById(R.id.attachDoc);
        attachDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        address = findViewById(R.id.address);
        contactNumber = findViewById(R.id.contactNumber);
        btnProceed = findViewById(R.id.btnProceed);
        selectDate = findViewById(R.id.selectDate);
        advPayment= findViewById(R.id.advPayment);
        email = findViewById(R.id.email);
        if (mAuth != null) {
            currentUser = mAuth.getCurrentUser();
            email.setText(currentUser.getEmail());
            email.setEnabled(false);
        } else {
            email.setEnabled(true);
        }

        if(advancePayment!=null){
            advPayment.setText(advancePayment + " in Rupees");
        }else{
            advPayment.setText(rateValue + " in Rupees");
        }

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(BookVehicleActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                selectDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }

        });

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstNameVal = firstName.getText().toString();
                String lastNameVal = lastName.getText().toString();
                String licenceNumber = licenceNum.getText().toString();
                String addressVal = address.getText().toString();
                String contactNumberVal = contactNumber.getText().toString();
                String dateSelected = selectDate.getText().toString();

                if (!firstNameVal.isEmpty() && !lastNameVal.isEmpty() && !licenceNumber.isEmpty() && !addressVal.isEmpty()
                        && !contactNumberVal.isEmpty()
                        && !dateSelected.isEmpty() && bitmapArray != null) {
                    Intent confirmationIntent = new Intent(BookVehicleActivity.this, PaymentActivity.class);
                    confirmationIntent.putExtra("firstName", firstNameVal);
                    confirmationIntent.putExtra("lastNameVal", lastNameVal);
                    confirmationIntent.putExtra("licenceNumber", licenceNumber);
                    confirmationIntent.putExtra("addressVal", addressVal);
                    confirmationIntent.putExtra("contactNumberVal", contactNumberVal);
                    confirmationIntent.putExtra("dateSelected", dateSelected);
                    confirmationIntent.putExtra("userIdVal", userId);
                    confirmationIntent.putExtra("advnPay", advancePayment);
                    confirmationIntent.putExtra("ownerNumber",ownerNumber);
//                    confirmationIntent.putExtra("bitmapArray", bitmapArray);
                    startActivity(confirmationIntent);


                } else {
                    Toast.makeText(BookVehicleActivity.this, "Please enter all required values", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectImage() {


        final CharSequence[] options = {"Take Photo","Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(BookVehicleActivity.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(BookVehicleActivity.this, getApplicationContext().getPackageName() + ".my.package.name.provider", f));

                    startActivityForResult(intent, 1);

                } else if (options[item].equals("Gallery"))

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

                    attachDoc.setText(f.getAbsolutePath());
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
                attachDoc.setText(path);
                Bitmap thumbnail = (BitmapFactory.decodeFile(path));

                Log.w("path of image from gal.", path + "");

                bitmapArray = BitMapToString(thumbnail);

            }
        }
    }


    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
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
}
