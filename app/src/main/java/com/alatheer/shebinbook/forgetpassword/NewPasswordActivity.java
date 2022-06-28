package com.alatheer.shebinbook.forgetpassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.databinding.ActivityNewPasswordBinding;

public class NewPasswordActivity extends AppCompatActivity {
    String phone,new_password;
    ActivityNewPasswordBinding activityNewPasswordBinding;
    NewPasswordViewModel newPasswordViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        activityNewPasswordBinding = DataBindingUtil.setContentView(this,R.layout.activity_new_password);
        newPasswordViewModel = new NewPasswordViewModel(this);
        activityNewPasswordBinding.setNewpasswordviewmodel(newPasswordViewModel);
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        phone = getIntent().getStringExtra("phone");

    }

    public void confirm(View view) {
        new_password = activityNewPasswordBinding.etNewPassword.getText().toString();
        if (new_password.length()>= 8){
            newPasswordViewModel.set_new_password(phone,new_password);
        }else {
            activityNewPasswordBinding.etNewPassword.setError("كلمة المرور ضعيفة لابد ان تكون 8 حروف أو أرقام");
        }
    }
}