package com.app.ssoft.vrs.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vehicle);
        sourceET = findViewById(R.id.sourceET);
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
                if(!source.isEmpty() && !destination.isEmpty()) {
                    Intent intent = new Intent();
                    intent.putExtra("source", source);
                    intent.putExtra("dest", destination);
                    intent.putExtra("vehicleType", vehType);
                    setResult(2, intent);
                    finish();//finishing activity
                }else {
                    Toast.makeText(SearchVehicleActivity.this,"All fields are required",Toast.LENGTH_SHORT).show();
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
