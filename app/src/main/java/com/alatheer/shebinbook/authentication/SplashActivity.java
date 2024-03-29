package com.alatheer.shebinbook.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.authentication.login.LoginActivity;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.home.HomeActivity;
import com.alatheer.shebinbook.start.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashActivity extends AppCompatActivity {
    ImageView logo_img;
    MySharedPreference mySharedPreference;
    LoginModel loginModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo_img = findViewById(R.id.logo_img);
        mySharedPreference = MySharedPreference.getInstance();
        loginModel = mySharedPreference.Get_UserData(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loginModel != null){
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent.putExtra("flag", 1);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(SplashActivity.this, StartActivity.class);
                    intent.putExtra("flag", 1);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }
}