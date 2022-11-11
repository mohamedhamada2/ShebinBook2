package com.alatheer.shebinbook.authentication.signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.alatheer.shebinbook.Utilities.Utilities;
import com.alatheer.shebinbook.api.GetDataService;
import com.alatheer.shebinbook.api.MySharedPreference;
import com.alatheer.shebinbook.api.RetrofitClientInstance;
import com.alatheer.shebinbook.authentication.login.LoginModel;
import com.alatheer.shebinbook.forgetpassword.NewPassword;
import com.alatheer.shebinbook.home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.annotation.NonNull;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationCodeViewModel {
    Context context;
    VerificationCodeActivity verificationCodeActivity;
    MySharedPreference mprefs;
    LoginModel loginModel;
    String firebase_token;

    public VerificationCodeViewModel(Context context) {
        this.context = context;
        verificationCodeActivity = (VerificationCodeActivity) context;
    }

    public void sendRegisterRequestwithImage(String first_name, String last_name, String phone, String password, Uri filepath, String city_id, String gender_id) {
        RequestBody rb_first_name = Utilities.getRequestBodyText(first_name+"");
        RequestBody rb_last_name = Utilities.getRequestBodyText(last_name);
        RequestBody rb_phone = Utilities.getRequestBodyText(phone+ "");
        RequestBody rb_password = Utilities.getRequestBodyText(password+"");
        RequestBody rb_city_id= Utilities.getRequestBodyText(city_id+"");
        RequestBody rb_gender= Utilities.getRequestBodyText(gender_id+"");
        MultipartBody.Part img = Utilities.getMultiPart(context, filepath, "user_img");
        if (Utilities.isNetworkAvailable(context)) {
            ProgressDialog pd = new ProgressDialog(verificationCodeActivity);
            pd.setMessage("loading ...");
            pd.show();
            mprefs = MySharedPreference.getInstance();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<LoginModel> call = getDataService.add_user_with_img(rb_first_name, rb_last_name, rb_phone, rb_password,rb_gender,rb_city_id,img);
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    if(response.isSuccessful()){
                        if(response.body().getStatus()){
                            Toast.makeText(context, "تم تسجيلك بنجاح", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                            loginModel = response.body();
                            mprefs.Create_Update_UserData(context,loginModel);
                            send_weclome_notify(firebase_token);
                            //Toast.makeText(context, "login successfully", Toast.LENGTH_SHORT).show();

                        }else {
                            pd.dismiss();
                            Toast.makeText(context, response.body().getError(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {

                }
            });
        }
    }

    public void get_firebase_token() {
        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if(task.isSuccessful()){
                        firebase_token = task.getResult().getToken();
                        Log.e("firebase_token",firebase_token);
                        //update_user_token();

                        //mainViewModel.update_token(firebase_token);
                    }
                }
            });
        } catch (Exception e) {
            Log.e("exception_e",e.toString());
            e.printStackTrace();
        }
    }

    private void send_weclome_notify(String token) {
        GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<WelcomeNotifications> call = getDataService.welcome_notify(token);
        call.enqueue(new Callback<WelcomeNotifications>() {
            @Override
            public void onResponse(Call<WelcomeNotifications> call, Response<WelcomeNotifications> response) {
                if (response.isSuccessful()){
                    if (response.body().getSuccess()==1){
                        Log.e("nnnn",token);
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.putExtra("flag",2);
                        intent.putExtra("token",token);
                        //Toast.makeText(context, "token", Toast.LENGTH_SHORT).show();
                        context.startActivity(intent);
                        //Animatoo.animateFade(context);
                        verificationCodeActivity.finish();
                        SignupActivity.fa.finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<WelcomeNotifications> call, Throwable t) {

            }
        });
    }

    public void sendRegisterRequestwithoutImage(String first_name, String last_name, String phone, String password, String city_id, String gender_id) {
        if (Utilities.isNetworkAvailable(context)) {
            ProgressDialog pd = new ProgressDialog(verificationCodeActivity);
            pd.setMessage("loading ...");
            pd.show();
            mprefs = MySharedPreference.getInstance();
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<LoginModel> call = getDataService.add_user(first_name,last_name,phone,password,gender_id,city_id);
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    if(response.isSuccessful()){
                        if(response.body().getStatus()){
                            Toast.makeText(context, "تم تسجيلك بنجاح", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                            loginModel = response.body();
                            mprefs.Create_Update_UserData(context,loginModel);
                            send_weclome_notify(firebase_token);
                            //Toast.makeText(context, "login successfully", Toast.LENGTH_SHORT).show();
                            /*context.startActivity(new Intent(context, HomeActivity.class));
                            //Animatoo.animateFade(context);
                            verificationCodeActivity.finish();
                            SignupActivity.fa.finish();*/
                        }else {
                            pd.dismiss();
                            Toast.makeText(context, response.body().getError(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {

                }
            });
        }
    }

    public void check_phone(String phone) {
        if (Utilities.isNetworkAvailable(context)){
            GetDataService getDataService = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            Call<NewPassword> call = getDataService.check_phone(phone);
            call.enqueue(new Callback<NewPassword>() {
                @Override
                public void onResponse(Call<NewPassword> call, Response<NewPassword> response) {
                    if (response.isSuccessful()){
                        if (response.body().getSuccess()==1){
                            Toast.makeText(verificationCodeActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            verificationCodeActivity.sendVerificationCodeToUser("+2"+phone);
                        }else {
                            Toast.makeText(verificationCodeActivity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(verificationCodeActivity,SignupActivity.class));
                            verificationCodeActivity.finish();
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
