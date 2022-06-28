package com.alatheer.shebinbook.forgetpassword;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.signup.SignupActivity;
import com.alatheer.shebinbook.authentication.signup.VerificationCodeActivity;

public class ForgetPasswordActivity extends AppCompatActivity {
    String phone;
    EditText et_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        et_phone = findViewById(R.id.et_phone);
    }

    public void forget_password(View view) {
        phone = et_phone.getText().toString();

        if (!TextUtils.isEmpty(phone)){
            check_phone(phone);
        }
    }

    private void check_phone(String phone) {
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<NewPassword> call = getDataService.check_phone(phone);
        call.enqueue(new Callback<NewPassword>() {
            @Override
            public void onResponse(Call<NewPassword> call, Response<NewPassword> response) {
                if (response.isSuccessful()){
                    if (response.body().getSuccess()==0){
                        //Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgetPasswordActivity.this, VerificationCodeActivity.class);
                        intent.putExtra("phone",phone);
                        intent.putExtra("flag",3);
                        startActivity(intent);
                    }else {
                        Toast.makeText(ForgetPasswordActivity.this, "الرقم غير مسجل قم بإانشاء حساب لدينا", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgetPasswordActivity.this, SignupActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(Call<NewPassword> call, Throwable t) {

            }
        });
    }
}