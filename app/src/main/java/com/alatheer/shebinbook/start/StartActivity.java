package com.alatheer.shebinbook.start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.authentication.login.LoginActivity;
import com.alatheer.shebinbook.authentication.signup.SignupActivity;

public class StartActivity extends AppCompatActivity {
    Button btn_login,btn_sign_up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void Signup(View view) {
        startActivity(new Intent(StartActivity.this, SignupActivity.class));
    }

    public void Login(View view) {
        startActivity(new Intent(StartActivity.this, LoginActivity.class));
    }
}