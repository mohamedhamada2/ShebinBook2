package com.alatheer.shebinbook.authentication.login;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginActivity;
import com.alatheer.shebinbook.home.HomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel {
    Context context;
    LoginActivity loginActivity;
    LoginModel loginModel;
    MySharedPreference mprefs;

    public LoginViewModel(Context context) {
        this.context = context;
        loginActivity = (LoginActivity) context;
    }

    public void login_user(String phone, String password) {
        mprefs = MySharedPreference.getInstance();
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<LoginModel> call = getDataService.login_user(phone,password);
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            loginModel = response.body();
                            mprefs.Create_Update_UserData(context,loginModel);
                            Toast.makeText(context, "تم تسجيلك بنجاح", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("flag",1);
                            context.startActivity(intent);
                            //Animatoo.animateFade(context);
                            loginActivity.finish();
                        }else {
                            Toast.makeText(context, "بياناتك غير صحيحية", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {

                }
            });
        }
    }
}
