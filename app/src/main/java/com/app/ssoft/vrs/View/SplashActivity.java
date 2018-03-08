package com.app.ssoft.vrs.View;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ssoft.vrs.R;
import com.app.ssoft.vrs.Utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.intentfilter.androidpermissions.PermissionManager;

import static java.util.Collections.singleton;

public class SplashActivity extends AppCompatActivity {

    private TextView versionCode;
    private Intent myintent;
    private PermissionManager permissionManager;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        versionCode = findViewById(R.id.versionCode);
        versionCode.setText("Version - " + Constants.getVersionName(this));
        auth = FirebaseAuth.getInstance();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                permissionManager = PermissionManager.getInstance(SplashActivity.this);
                permissionManager.checkPermissions(singleton(Manifest.permission.WRITE_EXTERNAL_STORAGE), new PermissionManager.PermissionRequestListener() {
                    @Override
                    public void onPermissionGranted() {
                        permissionManager.checkPermissions(singleton(Manifest.permission.CAMERA), new PermissionManager.PermissionRequestListener() {
                            @Override
                            public void onPermissionGranted() {
                                if (auth.getCurrentUser() != null) {
                                    myintent = new Intent(SplashActivity.this, MainActivity.class);
                                    startActivity(myintent);
                                    finish();
                                } else {
                                    myintent = new Intent(SplashActivity.this, LoginActivity.class);
                                    startActivity(myintent);
                                    finish();
                                }
                            }

                            @Override
                            public void onPermissionDenied() {
                                Toast.makeText(SplashActivity.this, "Required permission to access file manager", Toast.LENGTH_SHORT).show();
                                startActivity(myintent);
                                finish();
                            }


                        });
                    }

                    @Override
                    public void onPermissionDenied() {
                        Toast.makeText(SplashActivity.this, "Required permission to access file manager", Toast.LENGTH_SHORT).show();
                        startActivity(myintent);
                        finish();
                    }
                });



            }
        }, 1000);
    }


}