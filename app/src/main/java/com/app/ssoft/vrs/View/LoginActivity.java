package com.app.ssoft.vrs.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.ssoft.vrs.R;
import com.app.ssoft.vrs.Utils.Utils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUserName;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String userID = etUserName.getText().toString();
        String password = etPassword.getText().toString();
        if (!userID.isEmpty() && !password.isEmpty()) {
            if (Utils.validEmail(userID)) {

            } else {
                Toast.makeText(this, R.string.email_error_msg, Toast.LENGTH_SHORT).show();
            }
        } else {
            Intent intent = new Intent(this,VehicleDetailsActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Email/Password required", Toast.LENGTH_SHORT).show();
        }
    }
}
