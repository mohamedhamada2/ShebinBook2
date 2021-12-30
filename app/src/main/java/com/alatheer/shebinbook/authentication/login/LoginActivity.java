package com.alatheer.shebinbook.authentication.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.authentication.signup.SignupActivity;
import com.alatheer.shebinbook.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding activityLoginBinding;
    LoginViewModel loginViewModel;
    String phone,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activityLoginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        loginViewModel = new LoginViewModel(this);
        activityLoginBinding.setLoginviewmodel(loginViewModel);
        activityLoginBinding.txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(LoginActivity.this, SignupActivity.class));
                intent.putExtra("flag",1);
                startActivity(intent);
            }
        });
        activityLoginBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });

    }

    private void validation() {
        phone = activityLoginBinding.etPhone.getText().toString();
        password = activityLoginBinding.etPassword.getText().toString();
        if (!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(password)){
            loginViewModel.login_user(phone,password);
        }else {
            if (TextUtils.isEmpty(phone)){
                activityLoginBinding.etPhone.setError("برجاء إدخال رقم الهاتف");
            }else {
                activityLoginBinding.etPhone.setError(null);
            }
            if (TextUtils.isEmpty(password)){
                activityLoginBinding.etPassword.setError("برجاء إدخال الرقم السري");
            }else {
                activityLoginBinding.etPassword.setError(null);
            }
        }
    }
}