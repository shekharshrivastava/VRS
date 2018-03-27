package com.app.ssoft.vrs.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.ssoft.vrs.R;
import com.app.ssoft.vrs.Utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUserName;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btn_reset_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btn_reset_password = findViewById(R.id.btn_reset_password);
        btn_reset_password.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                String userID = etUserName.getText().toString();
                final String password = etPassword.getText().toString();
                if (!userID.isEmpty() && !password.isEmpty()) {
                    if (Utils.validEmail(userID)) {
                        progressBar.setVisibility(View.VISIBLE);
                        etUserName.setEnabled(false);
                        etPassword.setEnabled(false);
                        btnLogin.setEnabled(false);
                        btnRegister.setEnabled(false);
                        auth.signInWithEmailAndPassword(userID, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // If sign in fails, display a message to the user. If sign in succeeds
                                        // the auth state listener will be notified and logic to handle the
                                        // signed in user can be handled in the listener.
//                                    progressBar.setVisibility(View.GONE);
                                        if (!task.isSuccessful()) {
                                            progressBar.setVisibility(View.GONE);
                                            etUserName.setEnabled(true);
                                            etPassword.setEnabled(true);
                                            btnLogin.setEnabled(true);
                                            btnRegister.setEnabled(true);
                                            // there was an error
                                            if (password.length() < 6) {
//                                            inputPassword.setError(getString(R.string.minimum_password));
                                            } else {
                                                Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            progressBar.setVisibility(View.GONE);

                                            auth.getCurrentUser();
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });

                    } else {
                        Toast.makeText(this, R.string.email_error_msg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Email/Password required", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnRegister:
                Intent intent = new Intent(this, RegistrationActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_reset_password:
                Intent resetPWDIntent = new Intent(this, ResetPasswordActivity.class);
                startActivity(resetPWDIntent);
                break;
        }
    }
}
