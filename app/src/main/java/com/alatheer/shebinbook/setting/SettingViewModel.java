package com.alatheer.shebinbook.setting;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingViewModel {
    Context context;
    SettingActivity settingActivity;
    LoginModel loginModel;

    public SettingViewModel(Context context) {
        this.context = context;
        settingActivity = (SettingActivity) context;
    }

    public void getData(String user_id) {
        if (Utilities.isNetworkAvailable(context)){
            ProgressDialog pd = new ProgressDialog(settingActivity);
            pd.setMessage("loading ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ProfileData> call = getDataService.get_user_data(user_id);
            call.enqueue(new Callback<ProfileData>() {
                @Override
                public void onResponse(Call<ProfileData> call, Response<ProfileData> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            settingActivity.setData(response.body());
                            pd.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProfileData> call, Throwable t) {
                    Log.e("getdata",t.getMessage());
                }
            });
        }
    }

    public void updateuserwithImage(String user_id, String user_first_name, String user_last_name, String user_phone, String new_password, Uri filepath, String s, String gender) {
        if (Utilities.isNetworkAvailable(context)){
            ProgressDialog pd = new ProgressDialog(settingActivity);
            pd.setMessage("loading ...");
            pd.show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            RequestBody rb_user_id = Utilities.getRequestBodyText(user_id);
            RequestBody rb_first_name = Utilities.getRequestBodyText(user_first_name);
            RequestBody rb_last_name = Utilities.getRequestBodyText(user_last_name);
            RequestBody rb_phone = Utilities.getRequestBodyText(user_phone);
            RequestBody rb_password = Utilities.getRequestBodyText(new_password);
            RequestBody rb_city_id= Utilities.getRequestBodyText(s);
            RequestBody rb_gender= Utilities.getRequestBodyText(gender);
            MultipartBody.Part img = Utilities.getMultiPart(context, filepath, "user_img");
            Call<ProfileData> call = getDataService.update_user_with_img(rb_user_id,rb_first_name,rb_last_name,rb_phone,rb_password,rb_gender,rb_city_id,img);
            call.enqueue(new Callback<ProfileData>() {
                @Override
                public void onResponse(Call<ProfileData> call, Response<ProfileData> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            Toast.makeText(settingActivity, "تم تعديل بياناتك بنجاح", Toast.LENGTH_SHORT).show();
                            getData(user_id);
                            pd.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProfileData> call, Throwable t) {

                }
            });
        }
    }

    public void updateuserwithoutImage(String user_id, String user_first_name, String user_last_name, String user_phone, String new_password, String s, String gender) {
        if (Utilities.isNetworkAvailable(context)){
            Toast.makeText(settingActivity,gender, Toast.LENGTH_SHORT).show();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<ProfileData> call = getDataService.update_user_without_img(user_id,user_first_name,user_last_name,user_phone,new_password,gender,s);
            call.enqueue(new Callback<ProfileData>() {
                @Override
                public void onResponse(Call<ProfileData> call, Response<ProfileData> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus()){
                            Toast.makeText(settingActivity, "تم تعديل بياناتك بنجاح", Toast.LENGTH_SHORT).show();
                            getData(user_id);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProfileData> call, Throwable t) {

                }
            });
        }
    }
}
