package com.app.ssoft.vrs.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.app.ssoft.vrs.R;
import com.app.ssoft.vrs.Utils.Utils;

public class ImageViewActivity extends AppCompatActivity {


    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vehicle Document");
        imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(Utils.StringToBitMap(VehicleDetailsActivity.docBitmapArray));


    }
}
