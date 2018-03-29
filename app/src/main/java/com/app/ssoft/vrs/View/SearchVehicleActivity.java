package com.app.ssoft.vrs.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ssoft.vrs.R;

public class SearchVehicleActivity extends AppCompatActivity {

    private EditText destEt;
    private EditText sourceET;
    private TextView dateTv;
    private Button btnSearch;
    private String vehType = "Car";
    private RadioGroup radioGroup;
    private RadioButton tvVehTypeCar;
    private RadioButton tvVehTypeBike;
    private RadioGroup radioGroupRoute;
    private RadioButton rbVariable;
    private RadioButton rbFixed;
    private CardView cardViewDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vehicle);
        radioGroupRoute = findViewById(R.id.radioGroupRoute);
        rbVariable = findViewById(R.id.rbVariable);
        rbFixed = findViewById(R.id.rbFixed);
        sourceET = findViewById(R.id.sourceET);
        cardViewDestination = findViewById(R.id.cardViewDestination);
        destEt = findViewById(R.id.destET);
        dateTv = findViewById(R.id.dateTV);
        btnSearch = findViewById(R.id.searchBtn);
        radioGroup = findViewById(R.id.radioGroup);
        tvVehTypeBike = findViewById(R.id.tvVehTypeBike);
        tvVehTypeCar = findViewById(R.id.tvVehTypeCar);
        getSupportActionBar().setTitle("Search Ride");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String source = sourceET.getText().toString();
                String destination = destEt.getText().toString();
                if(rbFixed.isChecked() && tvVehTypeCar.isChecked()) {
                    if (TextUtils.isEmpty(destination)) {
                        Toast.makeText(getApplicationContext(), "Enter Destination !", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(source)) {
                        Toast.makeText(getApplicationContext(), "Enter Source !", Toast.LENGTH_SHORT).show();
                        return;
                    }
                        Intent intent = new Intent();
                        intent.putExtra("source", source);
                        intent.putExtra("dest", destination);
                        intent.putExtra("vehicleType", vehType);
                        setResult(2, intent);
                        finish();//finishing activity
                    }else{
                    Intent intent = new Intent();
                    intent.putExtra("source", source);
                    intent.putExtra("dest", "");
                    intent.putExtra("vehicleType", vehType);
                    setResult(2, intent);
                    finish();
                }

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.tvVehTypeBike:
                        vehType = (String) tvVehTypeBike.getText();
                        rbVariable.setChecked(true);
                        rbFixed.setEnabled(false);
                        cardViewDestination.setVisibility(View.GONE);
                        break;
                    case R.id.tvVehTypeCar:
                        rbFixed.setEnabled(true);
                        vehType = (String) tvVehTypeCar.getText();
                        if(rbFixed.isChecked()) {
                            cardViewDestination.setVisibility(View.VISIBLE);
                        }else{
                            cardViewDestination.setVisibility(View.GONE);
                        }
                        break;

                }
            }
        });

        radioGroupRoute.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbFixed:
                        cardViewDestination.setVisibility(View.VISIBLE);

                        break;
                    case R.id.rbVariable:
                        cardViewDestination.setVisibility(View.GONE);
                        break;

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
