package com.alatheer.shebinbook.forgetpassword;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPasswordViewModel {
    Context context;
    NewPasswordActivity newPasswordActivity;

    public NewPasswordViewModel(Context context) {
        this.context = context;
        newPasswordActivity = (NewPasswordActivity) context;
    }

    public void set_new_password(String phone,String password) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<NewPassword> call = getDataService.new_password(phone,password);
            call.enqueue(new Callback<NewPassword>() {
                @Override
                public void onResponse(Call<NewPassword> call, Response<NewPassword> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess()==1){
                            Toast.makeText(newPasswordActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(newPasswordActivity, LoginActivity.class));
                            newPasswordActivity.finish();
                        }else{
                            Toast.makeText(newPasswordActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<NewPassword> call, Throwable t) {

                }
            });
        }
    }

}
